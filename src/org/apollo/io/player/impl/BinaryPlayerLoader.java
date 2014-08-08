package org.apollo.io.player.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.model.Appearance;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.game.model.entity.attr.Attribute;
import org.apollo.game.model.entity.attr.AttributeType;
import org.apollo.game.model.entity.attr.BooleanAttribute;
import org.apollo.game.model.entity.attr.NumericalAttribute;
import org.apollo.game.model.entity.attr.StringAttribute;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.setting.Gender;
import org.apollo.game.model.setting.PrivacyState;
import org.apollo.game.model.setting.PrivilegeLevel;
import org.apollo.game.model.setting.ScreenBrightness;
import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.NameUtil;
import org.apollo.util.StreamUtil;

import com.lambdaworks.crypto.SCryptUtil;

/**
 * A {@link PlayerLoader} implementation that loads data from a binary file.
 * 
 * @author Graham
 */
public final class BinaryPlayerLoader implements PlayerLoader {

	/**
	 * The default spawn position.
	 */
	private static final Position SPAWN_POSITION = new Position(3093, 3104);

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws IOException {
		File file = BinaryPlayerUtil.getFile(credentials.getUsername());
		if (!file.exists()) {
			Player player = new Player(credentials, SPAWN_POSITION);
			player.getBank().add(995, 25); // 25 coins
			credentials.setPassword(SCryptUtil.scrypt(credentials.getPassword(), 16384, 8, 1));
			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}

		try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
			// read credentials and privileges
			String name = StreamUtil.readString(in);
			String pass = StreamUtil.readString(in);

			if (!name.equalsIgnoreCase(credentials.getUsername()) || !SCryptUtil.check(credentials.getPassword(), pass)) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			// set the credentials password to the scrypted one
			credentials.setPassword(pass);

			PrivilegeLevel privilegeLevel = PrivilegeLevel.valueOf(in.readByte());
			boolean members = in.readBoolean();

			// read settings
			PrivacyState chatPrivacy = PrivacyState.valueOf(in.readByte(), true);
			PrivacyState friendPrivacy = PrivacyState.valueOf(in.readByte(), false);
			PrivacyState tradePrivacy = PrivacyState.valueOf(in.readByte(), false);
			int runEnergy = in.readByte();
			ScreenBrightness brightness = ScreenBrightness.valueOf(in.readByte());

			// read position
			int x = in.readUnsignedShort();
			int y = in.readUnsignedShort();
			int height = in.readUnsignedByte();

			// read appearance
			boolean designed = in.readBoolean();

			int genderIntValue = in.readUnsignedByte();
			Gender gender = genderIntValue == Gender.MALE.toInteger() ? Gender.MALE : Gender.FEMALE;
			int[] style = new int[7];
			for (int i = 0; i < style.length; i++) {
				style[i] = in.readUnsignedByte();
			}
			int[] colors = new int[5];
			for (int i = 0; i < colors.length; i++) {
				colors[i] = in.readUnsignedByte();
			}

			Player player = new Player(credentials, new Position(x, y, height));
			player.setPrivilegeLevel(privilegeLevel);
			player.setMembers(members);
			player.setChatPrivacy(chatPrivacy);
			player.setFriendPrivacy(friendPrivacy);
			player.setTradePrivacy(tradePrivacy);
			player.setRunEnergy(runEnergy);
			player.setScreenBrightness(brightness);

			player.setNew(designed);
			player.setAppearance(new Appearance(gender, style, colors));

			// read inventories
			readInventory(in, player.getInventory());
			readInventory(in, player.getEquipment());
			readInventory(in, player.getBank());

			// read skills
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
			for (int i = 0; i < ignoreCount; i++) {
				ignores.add(NameUtil.decodeBase37(in.readLong()));
			}
			player.setIgnoredUsernames(ignores);

			Map<String, Attribute<?>> attributes = readAttributes(in);
			attributes.forEach(player::setAttribute);

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}
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
		Attribute<?> attribute;

		for (int i = 0; i < count; i++) {
			String name = StreamUtil.readString(in);
			AttributeType type = AttributeType.valueOf(in.read());
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

}