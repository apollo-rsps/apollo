package org.apollo.net.release.r377;

import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.model.Skill;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link UpdateSkillEvent}.
 * @author Graham
 */
public final class UpdateSkillEventEncoder extends EventEncoder<UpdateSkillEvent> {

	@Override
	public GamePacket encode(UpdateSkillEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(49);
		Skill skill = event.getSkill();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getId());
		builder.put(DataType.BYTE, skill.getCurrentLevel());
		builder.put(DataType.INT, (int) skill.getExperience());

		return builder.toGamePacket();
	}

}
