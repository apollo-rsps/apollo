package org.apollo.game.release.r377;

import org.apollo.game.message.impl.UpdateSkillMessage;
import org.apollo.game.model.entity.Skill;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateSkillMessage}.
 *
 * @author Graham
 */
public final class UpdateSkillMessageEncoder extends MessageEncoder<UpdateSkillMessage> {

	@Override
	public GamePacket encode(UpdateSkillMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(49);
		Skill skill = message.getSkill();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getId());
		builder.put(DataType.BYTE, skill.getCurrentLevel());
		builder.put(DataType.INT, (int) skill.getExperience());

		return builder.toGamePacket();
	}

}
