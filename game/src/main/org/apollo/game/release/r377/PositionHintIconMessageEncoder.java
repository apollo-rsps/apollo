package org.apollo.game.release.r377;

import org.apollo.game.message.impl.HintIconMessage;
import org.apollo.game.message.impl.PositionHintIconMessage;
import org.apollo.game.message.impl.HintIconMessage.Type;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PositionHintIconMessage}.
 *
 * @author Major
 */
public final class PositionHintIconMessageEncoder extends MessageEncoder<PositionHintIconMessage> {

	@Override
	public GamePacket encode(PositionHintIconMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(199);
		HintIconMessage.Type type = message.getType();

		if (type == Type.PLAYER || type == Type.NPC) {
			throw new IllegalStateException("Unsupported hint icon type " + type + ".");
		}

		builder.put(DataType.BYTE, type.getValue());

		Position position = message.getPosition();
		builder.put(DataType.SHORT, position.getX());
		builder.put(DataType.SHORT, position.getY());
		builder.put(DataType.BYTE, message.getHeight());

		return builder.toGamePacket();
	}

}