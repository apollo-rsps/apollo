package org.apollo.io.player.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apollo.game.model.Appearance;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.io.player.PlayerSaver;
import org.apollo.util.NameUtil;
import org.apollo.util.StreamUtil;

import com.lambdaworks.crypto.SCryptUtil;

/**
 * A {@link PlayerSaver} implementation that saves player data to a binary file.
 * 
 * @author Graham
 */
public final class BinaryPlayerSaver implements PlayerSaver {

	@Override
	public void savePlayer(Player player) throws IOException {
		File file = BinaryPlayerUtil.getFile(player.getUsername());

		try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
			// write credentials and privileges
			StreamUtil.writeString(out, player.getUsername());
			StreamUtil.writeString(out, player.getCredentials().getCryptedPassword());
			out.writeByte(player.getPrivilegeLevel().toInteger());
			out.writeBoolean(player.isMembers());

			// write settings
			out.writeByte(player.getChatPrivacy().toInteger(true));
			out.writeByte(player.getFriendPrivacy().toInteger(false));
			out.writeByte(player.getTradePrivacy().toInteger(false));
			out.writeByte(player.getRunEnergy());
			out.writeByte(player.getScreenBrightness().toInteger());

			// write position
			Position position = player.getPosition();
			out.writeShort(position.getX());
			out.writeShort(position.getY());
			out.writeByte(position.getHeight());

			// write appearance
			out.writeBoolean(player.isNewPlayer());
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
			out.flush();

			// write inventories
			writeInventory(out, player.getInventory());
			writeInventory(out, player.getEquipment());
			writeInventory(out, player.getBank());

			// write skills
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