package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SendPublicTileItemMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendPublicTileItemMessage}.
 *
 * @author Major
 */
public final class AddGlobalTileItemMessageEncoder extends MessageEncoder<SendPublicTileItemMessage> {

	@Override
	public GamePacket encode(SendPublicTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(106);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getAmount());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getIndex());
		return builder.toGamePacket();
	}

}