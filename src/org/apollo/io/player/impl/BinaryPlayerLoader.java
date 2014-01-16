package org.apollo.io.player.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apollo.game.model.Appearance;
import org.apollo.game.model.Gender;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;
import org.apollo.game.model.Position;
import org.apollo.game.model.PrivacyState;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.security.PlayerCredentials;
import org.apollo.util.StreamUtil;

/**
 * A {@link PlayerLoader} implementation that loads data from a binary file.
 * 
 * @author Graham
 */
public final class BinaryPlayerLoader implements PlayerLoader {

	/**
	 * The default spawn position.
	 */
	private static final Position SPAWN_POSITION = new Position(3222, 3222);

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws IOException {
		File f = BinaryPlayerUtil.getFile(credentials.getUsername());
		if (!f.exists()) {
			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, new Player(credentials, SPAWN_POSITION));
		}

		DataInputStream in = new DataInputStream(new FileInputStream(f));

		try {
			// read credentials and privileges
			String name = StreamUtil.readString(in);
			String pass = StreamUtil.readString(in);

			if (!name.equalsIgnoreCase(credentials.getUsername()) || !pass.equalsIgnoreCase(credentials.getPassword())) {
				return new PlayerLoaderResponse(LoginConstants.STATUS_INVALID_CREDENTIALS);
			}

			PrivilegeLevel privilegeLevel = PrivilegeLevel.valueOf(in.readByte());
			boolean members = in.readBoolean();

			// read settings
			PrivacyState privacyPublicChat = PrivacyState.valueOf(in.readByte());
			PrivacyState privacyPrivateChat = PrivacyState.valueOf(in.readByte());
			PrivacyState privacyTradeCompete = PrivacyState.valueOf(in.readByte());
            byte screenBrightness = in.readByte();

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
			player.setPublicChatPrivacy(privacyPublicChat);
			player.setPrivateChatPrivacy(privacyPrivateChat);
			player.setTradeChatPrivacy(privacyTradeCompete);
            player.setScreenBrightness(screenBrightness);
			player.setDesigned(designed);
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

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		} finally {
			in.close();
		}
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