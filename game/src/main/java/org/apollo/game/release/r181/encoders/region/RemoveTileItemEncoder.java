package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.AddTileItemMessage;
import org.apollo.game.message.impl.encode.RemoveTileItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class RemoveTileItemEncoder extends MessageEncoder<RemoveTileItemMessage> {

	@Override
	public GamePacket encode(RemoveTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(14);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}

}