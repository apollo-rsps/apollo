package org.apollo.game.io.player;

import com.lambdaworks.crypto.SCryptUtil;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.game.model.entity.attr.*;
import org.apollo.game.model.entity.setting.*;
import org.apollo.game.model.inv.Inventory;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.NameUtil;
import org.apollo.util.StreamUtil;
import org.apollo.util.security.PlayerCredentials;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * A {@link PlayerSerializer} implementation that uses a binary file to store player data.
 *
 * @author Graham
 * @author Major
 */
public final class BinaryPlayerSerializer extends PlayerSerializer {

	/**
	 * Creates the BinaryPlayerSerializer.
	 *
	 * @param world The {@link World} to place the {@link Player}s in.
	 */
	public BinaryPlayerSerializer(World world) {
		super(world);
	}

	/**
	 * The Path to the saved games directory.
	 */
	private static final Path SAVED_GAMES_DIRECTORY = Paths.get("data/savedGames");

	static {
		try {
			if (!Files.exists(SAVED_GAMES_DIRECTORY)) {
				Files.createDirectory(SAVED_GAMES_DIRECTORY);
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Error creating saved games directory.", e);
		}
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws IOException {
		Path path = getFile(credentials.getUsername());
		if (!Files.exists(path)) {
			Player player = new Player(world, credentials, TUTORIAL_ISLAND_SPAWN);

			credentials.setPassword(SCryptUtil.scrypt(credentials.getPassword(), 16384, 8, 1));
			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}

		try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
			String name = StreamUtil.readString(in);
			String password = StreamUtil.readString(in);

			if (!name.equalsIgnoreCase(credentials.getUsername()) || !SCryptUtil.check(credentials.getPassword(), password)) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			credentials.setPassword(password); // Update password to the hashed one.

			PrivilegeLevel privilege = PrivilegeLevel.valueOf(in.readByte());
			MembershipStatus members = MembershipStatus.valueOf(in.readByte());

			PrivacyState chatPrivacy = PrivacyState.valueOf(in.readByte(), true);
			PrivacyState friendPrivacy = PrivacyState.valueOf(in.readByte(), false);
			PrivacyState tradePrivacy = PrivacyState.valueOf(in.readByte(), false);
			ScreenBrightness brightness = ScreenBrightness.valueOf(in.readByte());

			int x = in.readUnsignedShort();
			int y = in.readUnsignedShort();
			int height = in.readUnsignedByte();

			Gender gender = in.readUnsignedByte() == Gender.MALE.toInteger() ? Gender.MALE : Gender.FEMALE;
			int[] style = new int[7];
			for (int slot = 0; slot < style.length; slot++) {
				style[slot] = in.readUnsignedByte();
			}

			int[] colors = new int[5];
			for (int slot = 0; slot < colors.length; slot++) {
				colors[slot] = in.readUnsignedByte();
			}

			Player player = new Player(world, credentials, new Position(x, y, height));
			player.setPrivilegeLevel(privilege);
			player.setMembers(members);
			player.setChatPrivacy(chatPrivacy);
			player.setFriendPrivacy(friendPrivacy);
			player.setTradePrivacy(tradePrivacy);
			player.setScreenBrightness(brightness);

			player.setAppearance(new Appearance(gender, style, colors));

			readInventory(in, player.getInventory());
			readInventory(in, player.getEquipment());
			readInventory(in, player.getBank());

			int size = in.readUnsignedByte();
			SkillSet skills = player.getSkillSet();
			skills.stopFiringEvents();
			try {
				for (int i = 0; i < size; i++) {
					int level = in.readUnsignedByte();
					double experience = in.readDouble();
					skills.setSkill(i, new Skill(experience, level, SkillSet.getLevelForExperience(experience)));
				}
			} finally {
				skills.calculateCombatLevel();
				skills.startFiringEvents();
			}

			int friendCount = in.readByte();
			List<String> friends = new ArrayList<>(friendCount);
			for (int i = 0; i < friendCount; i++) {
				friends.add(NameUtil.decodeBase37(in.readLong()));
			}
			player.setFriendUsernames(friends);

			int ignoreCount = in.readByte();
			List<String> ignores = new ArrayList<>(ignoreCount);
			for (int times = 0; times < ignoreCount; times++) {
				ignores.add(NameUtil.decodeBase37(in.readLong()));
			}
			player.setIgnoredUsernames(ignores);

			Map<String, Attribute<?>> attributes = readAttributes(in);
			attributes.forEach(player::setAttribute);

			if (player.isBanned()) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_ACCOUNT_DISABLED);
			}

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}
	}

	@Override
	public void savePlayer(Player player) throws IOException {
		Path file = getFile(player.getUsername());

		try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(file))) {
			StreamUtil.writeString(out, player.getUsername());
			StreamUtil.writeString(out, player.getCredentials().getPassword());
			out.writeByte(player.getPrivilegeLevel().toInteger());
			out.writeByte(player.getMembershipStatus().getValue());

			out.writeByte(player.getChatPrivacy().toInteger(true));
			out.writeByte(player.getFriendPrivacy().toInteger(false));
			out.writeByte(player.getTradePrivacy().toInteger(false));
			out.writeByte(player.getScreenBrightness().toInteger());

			Position position = player.getPosition();
			out.writeShort(position.getX());
			out.writeShort(position.getY());
			out.writeByte(position.getHeight());

			Appearance appearance = player.getAppearance();
			out.writeByte(appearance.getGender().toInteger());
			int[] style = appearance.getStyle();
			for (int element : style) {
				out.writeByte(element);
			}
			int[] colors = appearance.getColors();
			for (int color : colors) {
				out.writeByte(color);
			}

			writeInventory(out, player.getInventory());
			writeInventory(out, player.getEquipment());
			writeInventory(out, player.getBank());

			SkillSet skills = player.getSkillSet();
			out.writeByte(skills.size());
			for (int id = 0; id < skills.size(); id++) {
				Skill skill = skills.getSkill(id);
				out.writeByte(skill.getCurrentLevel());
				out.writeDouble(skill.getExperience());
			}

			List<String> usernames = player.getFriendUsernames();
			out.writeByte(usernames.size());
			for (String username : usernames) {
				out.writeLong(NameUtil.encodeBase37(username));
			}

			usernames = player.getIgnoredUsernames();
			out.writeByte(usernames.size());
			for (String username : usernames) {
				out.writeLong(NameUtil.encodeBase37(username));
			}

			Set<Entry<String, Attribute<?>>> attributes = player.getAttributes().entrySet();
			attributes.removeIf(e -> AttributeMap.getDefinition(e.getKey()).getPersistence() != AttributePersistence.PERSISTENT);
			out.writeInt(attributes.size());

			for (Entry<String, Attribute<?>> entry : attributes) {
				String name = entry.getKey();
				StreamUtil.writeString(out, name);

				Attribute<?> attribute = entry.getValue();
				out.writeByte(attribute.getType().getValue());
				out.write(attribute.encode());
			}
		}
	}

	/**
	 * Gets the save {@link File} for the specified player.
	 *
	 * @param username The username of the player.
	 * @return The file.
	 */
	private Path getFile(String username) {
		String filtered = NameUtil.decodeBase37(NameUtil.encodeBase37(username));
		return SAVED_GAMES_DIRECTORY.resolve(filtered + ".dat");
	}

	/**
	 * Reads the player's {@link Attribute}s.
	 *
	 * @param in The input stream.
	 * @return The {@link Map} of attribute names to attributes.
	 * @throws IOException If there is an error reading from the stream.
	 */
	private Map<String, Attribute<?>> readAttributes(DataInputStream in) throws IOException {
		int count = in.readInt();
		Map<String, Attribute<?>> attributes = new HashMap<>(count);

		for (int times = 0; times < count; times++) {
			String name = StreamUtil.readString(in);
			AttributeType type = AttributeType.valueOf(in.read());
			Attribute<?> attribute;

			switch (type) {
				case BOOLEAN:
					attribute = new BooleanAttribute(in.read() == 1);
					break;
				case DOUBLE:
					attribute = new NumericalAttribute(in.readDouble());
					break;
				case LONG:
					attribute = new NumericalAttribute(in.readLong());
					break;
				case STRING:
				case SYMBOL:
					attribute = new StringAttribute(StreamUtil.readString(in), type == AttributeType.SYMBOL);
					break;
				default:
					throw new IllegalArgumentException("Undefined attribute type: " + type + ".");
			}
			attributes.put(name, attribute);
		}

		return attributes;
	}

	/**
	 * Reads an inventory from the input stream.
	 *
	 * @param in The input stream.
	 * @param inventory The inventory.
	 * @throws IOException If an I/O error occurs.
	 */
	private void readInventory(DataInputStream in, Inventory inventory) throws IOException {
		int capacity = in.readUnsignedShort();

		inventory.stopFiringEvents();
		try {
			for (int slot = 0; slot < capacity; slot++) {
				int id = in.readUnsignedShort();
				int amount = in.readInt();
				if (id != 0) {
					inventory.set(slot, new Item(id - 1, amount));
				} else {
					inventory.reset(slot);
				}
			}
		} finally {
			inventory.startFiringEvents();
		}
	}

	/**
	 * Writes an inventory to the specified output stream.
	 *
	 * @param out The output stream.
	 * @param inventory The inventory.
	 * @throws IOException If an I/O error occurs.
	 */
	private void writeInventory(DataOutputStream out, Inventory inventory) throws IOException {
		int capacity = inventory.capacity();
		out.writeShort(capacity);

		for (int slot = 0; slot < capacity; slot++) {
			Item item = inventory.get(slot);
			if (item != null) {
				out.writeShort(item.getId() + 1);
				out.writeInt(item.getAmount());
			} else {
				out.writeShort(0);
				out.writeInt(0);
			}
		}
	}

}