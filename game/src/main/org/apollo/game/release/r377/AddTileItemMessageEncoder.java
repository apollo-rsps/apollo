package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SendTileItemMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendTileItemMessage}.
 *
 * @author Major
 */
public final class AddTileItemMessageEncoder extends MessageEncoder<SendTileItemMessage> {

	@Override
	public GamePacket encode(SendTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(107);
		builder.put(DataType.SHORT, message.getId());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getPositionOffset());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getAmount());
		return builder.toGamePacket();
	}

}