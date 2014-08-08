package org.apollo.net.release.r377;

import org.apollo.game.message.impl.PositionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PositionMessage}.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class PositionMessageEncoder extends MessageEncoder<PositionMessage> {

	@Override
	public GamePacket encode(PositionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(75);
		Position base = message.getBase(), pos = message.getPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, pos.getLocalX(base));
		builder.put(DataType.BYTE, DataTransformation.ADD, pos.getLocalY(base));

		return builder.toGamePacket();
	}

}