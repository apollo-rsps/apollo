package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PlayerDesignEvent;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.settings.Gender;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link PlayerDesignEvent}.
 * 
 * @author Graham
 */
public final class PlayerDesignEventDecoder extends EventDecoder<PlayerDesignEvent> {

    @Override
    public PlayerDesignEvent decode(GamePacket packet) {
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

	return new PlayerDesignEvent(new Appearance(gender, style, color));
    }

}