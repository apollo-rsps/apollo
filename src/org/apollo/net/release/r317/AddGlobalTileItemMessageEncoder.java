package org.apollo.net.release.r317;

import org.apollo.game.message.impl.AddGlobalTileItemMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link AddGlobalTileItemMessage}.
 * 
 * @author Major
 */
public final class AddGlobalTileItemMessageEncoder extends MessageEncoder<AddGlobalTileItemMessage> {

	@Override
	public GamePacket encode(AddGlobalTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(215);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getPositionOffset());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getIndex());
		builder.put(DataType.SHORT, message.getAmount());
		return builder.toGamePacket();
	}

}