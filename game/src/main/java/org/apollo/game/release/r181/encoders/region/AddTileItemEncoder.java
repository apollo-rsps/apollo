package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.AddTileItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class AddTileItemEncoder extends MessageEncoder<AddTileItemMessage> {

	@Override
	public GamePacket encode(AddTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(26);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getAmount());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		return builder.toGamePacket();
	}

}