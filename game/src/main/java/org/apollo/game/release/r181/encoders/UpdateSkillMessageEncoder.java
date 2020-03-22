package org.apollo.game.release.r181.encoders;

import org.apollo.game.message.impl.encode.UpdateSkillMessage;
import org.apollo.game.model.entity.Skill;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateSkillMessage}.
 *
 * @author Khaled Abdeljaber
 */
public class UpdateSkillMessageEncoder extends MessageEncoder<UpdateSkillMessage> {
	@Override
	public GamePacket encode(UpdateSkillMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(22, PacketType.FIXED);
		Skill skill = message.getSkill();

		builder.put(DataType.INT, DataOrder.LITTLE, (int) skill.getExperience());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getId());
		builder.put(DataType.BYTE, skill.getCurrentLevel());

		return builder.toGamePacket();
	}
}
