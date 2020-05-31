package org.apollo.game.io.player;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lambdaworks.crypto.SCryptUtil;
import org.apollo.game.account.Account;
import org.apollo.game.account.Email;
import org.apollo.game.account.PasswordHash;
import org.apollo.game.database.ConnectionSupplier;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.game.model.entity.attr.*;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * A {@link PlayerSerializer} that utilises {@code JDBC} to communicate with an SQL database containing player data.
 *
 * @author Major
 * @author Sino
 */
// TODO optimize use of collections to avoid boxing by using fastutil
public final class JdbcPlayerSerializer extends PlayerSerializer {
	/**
	 * Maps skill id to the enum value that is known by the database
	 * and back.
	 */
	private static final ImmutableBiMap<Integer, String> SKILL_ENUMS = ImmutableBiMap.<Integer, String>builder()
			.put(Skill.ATTACK, "attack")
			.put(Skill.STRENGTH, "strength")
			.put(Skill.DEFENCE, "defence")
			.put(Skill.HITPOINTS, "hitpoints")
			.put(Skill.RANGED, "ranged")
			.put(Skill.PRAYER, "prayer")
			.put(Skill.MAGIC, "magic")
			.put(Skill.COOKING, "cooking")
			.put(Skill.FISHING, "fishing")
			.put(Skill.WOODCUTTING, "woodcutting")
			.put(Skill.FIREMAKING, "firemaking")
			.put(Skill.FLETCHING, "fletching")
			.put(Skill.FARMING, "farming")
			.put(Skill.SLAYER, "slayer")
			.put(Skill.AGILITY, "agility")
			.put(Skill.CRAFTING, "crafting")
			.put(Skill.HERBLORE, "herblore")
			.put(Skill.THIEVING, "thieving")
			.put(Skill.MINING, "mining")
			.put(Skill.SMITHING, "smithing")
			.put(Skill.RUNECRAFT, "runecraft")
			.build();

	/**
	 * Maps {@link PrivilegeLevel} to the enum value that is known by the
	 * database and back.
	 */
	private static final ImmutableBiMap<PrivilegeLevel, String> RANK_ENUMS = ImmutableBiMap.<PrivilegeLevel, String>builder()
			.put(PrivilegeLevel.STANDARD, "player")
			.put(PrivilegeLevel.MODERATOR, "moderator")
			.put(PrivilegeLevel.ADMINISTRATOR, "administrator")
			.build();

	/**
	 * Maps {@link AttributeType}s to the enum value that is known by the
	 * database and back.
	 */
	private static final ImmutableBiMap<AttributeType, String> ATTRIBUTE_TYPE_ENUMS = ImmutableBiMap.<AttributeType, String>builder()
			.put(AttributeType.STRING, "string")
			.put(AttributeType.BOOLEAN, "boolean")
			.put(AttributeType.DOUBLE, "double")
			.put(AttributeType.LONG, "long")
			.build();

	/**
	 * The associated queries that are ran by this serializer.
	 */
	private static final String
			GET_ACCOUNT_QUERY = "SELECT password_hash, rank FROM get_account(?::text)",
			GET_PLAYER_QUERY = "SELECT last_login, x, y, height FROM get_player(?::text)",
			GET_APPEARANCE_QUERY = "SELECT gender, styles, colours FROM get_appearance(?::text)",
			GET_ITEMS_QUERY = "SELECT inventory_id, slot, item_id, quantity FROM get_items(?::text)",
			GET_ATTRIBUTES_QUERY = "SELECT attr_type, name, value FROM get_attributes(?::text);",
			GET_STATS_QUERY = "SELECT skill, stat, experience FROM get_skills(?::text)",
			SET_ACCOUNT_QUERY = "CALL set_account(?::citext, ?::rank)",
			SET_PLAYER_QUERY = "CALL set_player(?::citext, ?::text, ?, ?, ?, ?)",
			SET_APPEARANCE_QUERY = "CALL set_appearance(?::text, ?::gender, ?, ?)",
			SET_STAT_QUERY = "CALL set_stat(?::skill, ?, ?, ?::text)",
			SET_ATTRIBUTE_QUERY = "CALL set_attribute(?::text, ?::attribute_type, ?::varchar, ?::text)",
			SET_ITEM_QUERY = "CALL set_item(?::text, ?, ?, ?, ?)",
			DELETE_ITEM_QUERY = "CALL delete_item(?::text, ?, ?)";

	/**
	 * The config id's of known inventories.
	 */
	private static final int ITEM_BAG_ID = 93,
			WORN_EQUIPMENT_ID = 94,
			BANK_ID = 95;

	/**
	 * A factory method to construct a new {@link JdbcPlayerSerializer}.
	 *
	 * @param world       The game world to feed to {@link Player} instances.
	 * @param connections The supplier of database connections.
	 * @return The {@link JdbcPlayerSerializer}.
	 */
	public static JdbcPlayerSerializer create(World world, ConnectionSupplier connections) {
		return new JdbcPlayerSerializer(world, connections);
	}

	/**
	 * Supplies this serializer with database connections.
	 */
	private final ConnectionSupplier connections;

	/**
	 * Creates the {@link JdbcPlayerSerializer}.
	 *
	 * @param world       The {@link World} to place the {@link Player}s in.
	 * @param connections The supplier of database connections.
	 */
	private JdbcPlayerSerializer(World world, ConnectionSupplier connections) {
		super(world);

		this.connections = connections;
	}

	@Override
	public void savePlayer(Player player) throws Exception {
		try (Connection connection = connections.get()) {
			Email email = Email.of(player.getCredentials().getUsername());
			String displayName = player.getCredentials().getUsername();

			putRank(connection, email, player.getPrivilegeLevel());
			putPlayer(connection, email, player);
			putAppearance(connection, displayName, player.getAppearance());
			putStats(connection, displayName, player.getSkillSet());

			putInventory(connection, displayName, ITEM_BAG_ID, player.getInventory());
			putInventory(connection, displayName, WORN_EQUIPMENT_ID, player.getEquipment());
			putInventory(connection, displayName, BANK_ID, player.getBank());

			putAttributes(connection, displayName, player.getAttributes());
		}
	}

	private void putRank(Connection connection, Email email, PrivilegeLevel rank) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(SET_ACCOUNT_QUERY)) {
			stmt.setString(1, email.getValue());
			stmt.setString(2, requireNonNull(RANK_ENUMS.get(rank)));
			stmt.execute();
		}
	}

	private void putPlayer(Connection connection, Email email, Player player) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(SET_PLAYER_QUERY)) {
			stmt.setString(1, email.getValue());
			stmt.setString(2, player.getCredentials().getUsername());
			stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			stmt.setInt(4, player.getPosition().getX());
			stmt.setInt(5, player.getPosition().getY());
			stmt.setInt(6, player.getPosition().getHeight());
			stmt.execute();
		}
	}

	private void putAppearance(Connection connection, String displayName, Appearance appearance) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(SET_APPEARANCE_QUERY)) {
			stmt.setString(1, displayName);
			stmt.setString(2, appearance.getGender().name().toLowerCase());
			stmt.setObject(3, appearance.getStyle());
			stmt.setObject(4, appearance.getColors());
			stmt.execute();
		}
	}

	private void putStats(Connection connection, String displayName, SkillSet skills) throws SQLException {
		for (int i = 0; i < Skill.getCount(); i++) {
			Skill skill = skills.getSkill(i);

			try (PreparedStatement stmt = connection.prepareStatement(SET_STAT_QUERY)) {
				stmt.setString(1, requireNonNull(SKILL_ENUMS.get(i)));
				stmt.setInt(2, skill.getCurrentLevel());
				stmt.setInt(3, (int) skill.getExperience());
				stmt.setString(4, displayName);
				stmt.execute();
			}
		}
	}

	private void putAttributes(Connection connection, String displayName, Map<String, Attribute<?>> attributes) throws SQLException {
		for (String key : attributes.keySet()) {
			AttributeDefinition<?> definition = AttributeMap.getDefinition(key);
			if (definition.getPersistence() == AttributePersistence.TRANSIENT) {
				continue;
			}

			Attribute<?> attribute = attributes.get(key);

			try (PreparedStatement stmt = connection.prepareStatement(SET_ATTRIBUTE_QUERY)) {
				stmt.setString(1, displayName);
				stmt.setString(2, requireNonNull(ATTRIBUTE_TYPE_ENUMS.get(attribute.getType())));
				stmt.setString(3, key);
				stmt.setString(4, String.valueOf(attribute.getValue()));
				stmt.execute();
			}
		}
	}

	private void putInventory(Connection connection, String displayName, int inventoryId, Inventory inventory) throws SQLException {
		for (int i = 0; i < inventory.capacity(); i++) {
			Item item = inventory.get(i);
			if (item == null) {
				deleteItem(connection, displayName, inventoryId, i);
			} else {
				putItem(connection, displayName, inventoryId, i, item);
			}
		}
	}

	private void putItem(Connection connection, String displayName, int inventoryId, int slot, Item item) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(SET_ITEM_QUERY)) {
			stmt.setString(1, displayName);
			stmt.setInt(2, inventoryId);
			stmt.setInt(3, slot);
			stmt.setInt(4, item.getId());
			stmt.setInt(5, item.getAmount());
			stmt.execute();
		}
	}

	private void deleteItem(Connection connection, String displayName, int inventoryId, int slot) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(DELETE_ITEM_QUERY)) {
			stmt.setString(1, displayName);
			stmt.setInt(2, inventoryId);
			stmt.setInt(3, slot);
			stmt.execute();
		}
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		try (Connection connection = connections.get()) {
			Account account = getAccount(connection, credentials.getUsername());
			if (account == null) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			String passwordInput = credentials.getPassword();
			String passwordHash = account.getPasswordHash().getValue();

			if (!SCryptUtil.check(passwordInput, passwordHash)) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			credentials.setPassword(passwordHash);

			Player player = getPlayer(connection, credentials);
			if (player == null) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE);
			}

			player.setPrivilegeLevel(account.getRank());
			player.setAppearance(requireNonNull(getAppearance(connection, credentials.getUsername())));

			loadItemsIntoPlayer(connection, credentials.getUsername(), player);
			loadStatsIntoPlayer(connection, credentials.getUsername(), player);
			loadAttributesIntoPlayer(connection, credentials.getUsername(), player);

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}
	}

	private void loadItemsIntoPlayer(Connection connection, String displayName, Player player) throws SQLException {
		ImmutableList<SlottedInventoryItem> items = getItems(connection, displayName);
		for (SlottedInventoryItem sii : items) {
			Inventory inventory;
			if (sii.getInventoryId() == ITEM_BAG_ID) {
				inventory = player.getInventory();
			} else if (sii.getInventoryId() == WORN_EQUIPMENT_ID) {
				inventory = player.getEquipment();
			} else if (sii.getInventoryId() == BANK_ID) {
				inventory = player.getBank();
			} else {
				// TODO add dynamic support for different types of inventories
				//  relying on inventory config id's
				continue;
			}

			inventory.stopFiringEvents();

			try {
				inventory.set(sii.getItem().getSlot(), sii.getItem().getItem());
			} finally {
				inventory.startFiringEvents();
			}
		}
	}

	private void loadStatsIntoPlayer(Connection connection, String displayName, Player player) throws SQLException {
		ImmutableList<Skill> skills = getStats(connection, displayName);

		// TODO database currently supports hunter and construction as well and 317 doesn't
		player.getSkillSet().stopFiringEvents();
		try {
			for (int i = 0; i < Skill.getCount(); i++) {
				player.getSkillSet().setSkill(i, skills.get(i));
			}
		} finally {
			player.getSkillSet().calculateCombatLevel();
			player.getSkillSet().startFiringEvents();
		}
	}

	private void loadAttributesIntoPlayer(Connection connection, String displayName, Player player) throws SQLException {
		ImmutableMap<String, Attribute<?>> attributes = getAttributes(connection, displayName);
		for (String name : attributes.keySet()) {
			player.setAttribute(name, attributes.get(name));
		}
	}

	private Account getAccount(Connection connection, String email) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_ACCOUNT_QUERY)) {
			stmt.setString(1, email);

			ResultSet results = stmt.executeQuery();
			if (!results.next()) {
				return null;
			}

			PasswordHash passwordHash = PasswordHash.of(results.getString("password_hash"));
			PrivilegeLevel rank = requireNonNull(RANK_ENUMS.inverse().get(results.getString("rank")));

			return Account.of(Email.of(email), passwordHash, rank);
		}
	}

	private Player getPlayer(Connection connection, PlayerCredentials credentials) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_PLAYER_QUERY)) {
			stmt.setString(1, credentials.getUsername());

			ResultSet results = stmt.executeQuery();
			if (!results.next()) {
				return null;
			}

			Timestamp timestamp = results.getTimestamp("last_login");
			if (timestamp != null) {
				// TODO convert
			}

			int x = results.getShort("x");
			int y = results.getShort("y");
			int height = results.getShort("height");

			Position position = new Position(x, y, height);
			Player plr = new Player(world, credentials, position);

			return plr;
		}
	}

	private Appearance getAppearance(Connection connection, String displayName) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_APPEARANCE_QUERY)) {
			stmt.setString(1, displayName);

			ResultSet results = stmt.executeQuery();
			if (!results.next()) {
				return null;
			}

			Gender gender = Gender.MALE;
			if ("woman".equals(results.getString("gender"))) {
				gender = Gender.FEMALE;
			}

			Short[] styleSet = (Short[]) results.getArray("styles").getArray();
			Short[] colourSet = (Short[]) results.getArray("colours").getArray();

			int[] styles = new int[7];
			for (int i = 0; i < styles.length; i++) {
				styles[i] = styleSet[i];
			}

			int[] colours = new int[5];
			for (int i = 0; i < colours.length; i++) {
				colours[i] = colourSet[i];
			}

			return new Appearance(gender, styles, colours);
		}
	}

	private ImmutableList<Skill> getStats(Connection connection, String displayName) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_STATS_QUERY)) {
			stmt.setString(1, displayName);

			ImmutableList.Builder<Skill> bldr = ImmutableList.builder();

			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				int stat = results.getInt("stat");
				int experience = results.getInt("experience");

				bldr.add(new Skill(experience, stat, SkillSet.getLevelForExperience(experience)));
			}

			return bldr.build();
		}
	}

	private ImmutableMap<String, Attribute<?>> getAttributes(Connection connection, String displayName) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_ATTRIBUTES_QUERY)) {
			stmt.setString(1, displayName);

			ImmutableMap.Builder<String, Attribute<?>> bldr = ImmutableMap.builder();

			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				AttributeType type = requireNonNull(ATTRIBUTE_TYPE_ENUMS.inverse().get(results.getString("attr_type")));

				String name = results.getString("name");
				String value = results.getString("value");

				Attribute<?> attribute;
				if (type == AttributeType.BOOLEAN) {
					attribute = new BooleanAttribute(Boolean.parseBoolean(value));
				} else if (type == AttributeType.DOUBLE) {
					attribute = new NumericalAttribute(Double.parseDouble(value));
				} else if (type == AttributeType.LONG) {
					attribute = new NumericalAttribute(Long.parseLong(value));
				} else if (type == AttributeType.STRING) {
					attribute = new StringAttribute(value);
				} else {
					throw new RuntimeException("Unsupported attribute type of " + type);
				}

				bldr.put(name, attribute);
			}

			return bldr.build();
		}
	}

	private ImmutableList<SlottedInventoryItem> getItems(Connection connection, String displayName) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_ITEMS_QUERY)) {
			stmt.setString(1, displayName);

			ImmutableList.Builder<SlottedInventoryItem> bldr = ImmutableList.builder();

			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				int itemId = results.getInt("item_id");
				int inventoryId = results.getInt("inventory_id");

				int slot = results.getInt("slot");
				int quantity = results.getInt("quantity");

				Item item = new Item(itemId, quantity);
				bldr.add(new SlottedInventoryItem(inventoryId, new SlottedItem(slot, item)));
			}

			return bldr.build();
		}
	}
}