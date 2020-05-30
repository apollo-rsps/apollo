package org.apollo.game.io.player;

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
import org.apollo.game.model.entity.attr.NumericalAttribute;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;

import java.sql.*;

import static java.util.Objects.requireNonNull;

/**
 * A {@link PlayerSerializer} that utilises {@code JDBC} to communicate with an SQL database containing player data.
 *
 * @author Major
 * @author Sino
 */
public final class JdbcPlayerSerializer extends PlayerSerializer {
	/**
	 * The associated queries that are ran by this serializer.
	 */
	private static final String
			GET_ACCOUNT_QUERY = "SELECT password_hash, rank FROM get_account(?::text)",
			GET_PLAYER_QUERY = "SELECT last_login, x, y, height, games_room_skill_lvl, energy_units FROM get_player(?::text)",
			GET_APPEARANCE_QUERY = "SELECT gender, styles, colours FROM get_appearance(?::text)",
			GET_ITEMS_QUERY = "SELECT inventory_id, slot, item_id, quantity FROM get_items(?::text)",
			GET_ATTRIBUTES_QUERY = "SELECT name, value FROM get_attributes(?::text);",
			GET_STATS_QUERY = "SELECT skill, stat, experience FROM get_skills(?::text)";

	/**
	 * The config id of the item bag inventory.
	 */
	private static final int ITEM_BAG_ID = 93;

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
			// TODO
		}
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		try (Connection connection = connections.get()) {
			Account account = getAccount(connection, credentials.getUsername());
			if (account == null) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			if (!SCryptUtil.check(credentials.getPassword(), account.getPasswordHash().getValue())) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			Player player = getPlayer(connection, credentials);
			if (player == null) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE);
			}

			player.setAppearance(requireNonNull(getAppearance(connection, credentials.getUsername())));

			ImmutableList<SlottedInventoryItem> items = getItems(connection, credentials.getUsername());
			for (SlottedInventoryItem sii : items) {
				// TODO add dynamic support for different types of inventories relying on config id's
				if (sii.getInventoryId() != ITEM_BAG_ID) {
					continue;
				}

				player.getInventory().set(sii.getItem().getSlot(), sii.getItem().getItem());
			}

			ImmutableList<Skill> skills = getStats(connection, credentials.getUsername());

			int skillCount = Skill.getCount(); // TODO database currently supports hunter and construction as well and 317 doesn't
			for (int i = 0; i < skillCount; i++) {
				player.getSkillSet().setSkill(i, skills.get(i));
			}

			// TODO add support for boolean and string attributes
			ImmutableMap<String, Integer> attributes = getAttributes(connection, credentials.getUsername());
			for (String name : attributes.keySet()) {
				player.setAttribute(name, new NumericalAttribute(attributes.get(name)));
			}

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
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
			PrivilegeLevel rank = toRank(results.getString("rank"));

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

			int energyUnits = results.getShort("energy_units");
			int gamesRoomSkillLvl = results.getShort("games_room_skill_lvl"); // TODO insert into player state

			plr.setRunEnergy(energyUnits / 100); // TODO use a static converter function to calculate this

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

	// TODO use fastutil's Object2IntMap instead
	private ImmutableMap<String, Integer> getAttributes(Connection connection, String displayName) throws SQLException {
		try (PreparedStatement stmt = connection.prepareStatement(GET_ATTRIBUTES_QUERY)) {
			stmt.setString(1, displayName);

			ImmutableMap.Builder<String, Integer> bldr = ImmutableMap.builder();

			ResultSet results = stmt.executeQuery();
			while (results.next()) {
				String name = results.getString("name");
				int value = results.getInt("value");

				bldr.put(name, value);
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

	// TODO consider a map?
	private PrivilegeLevel toRank(String value) {
		switch (value) {
			case "player":
				return PrivilegeLevel.STANDARD;

			case "moderator":
				return PrivilegeLevel.MODERATOR;

			case "administrator":
				return PrivilegeLevel.ADMINISTRATOR;

			default:
				throw new IllegalArgumentException("Unsupported rank ENUM type of value " + value);
		}
	}
}