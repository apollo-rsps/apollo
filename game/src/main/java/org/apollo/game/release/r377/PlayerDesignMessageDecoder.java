package org.apollo.game.release.r377;

import org.apollo.game.message.impl.PlayerDesignMessage;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link PlayerDesignMessage}.
 *
 * @author Graham
 */
public final class PlayerDesignMessageDecoder extends MessageDecoder<PlayerDesignMessage> {

	@Override
	public PlayerDesignMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int genderIntValue = (int) reader.getUnsigned(DataType.BYTE);

		int[] style = new int[7];
		for (int i = 0; i < style.length; i++) {
			style[i] = (int) reader.getUnsigned(DataType.BYTE);
		}

		int[] color = new int[5];
		for (int i = 0; i < color.length; i++) {
			color[i] = (int) reader.getUnsigned(DataType.BYTE);
		}

		Gender gender = genderIntValue == Gender.MALE.toInteger() ? Gender.MALE : Gender.FEMALE;

		return new PlayerDesignMessage(new Appearance(gender, style, color));
	}

}