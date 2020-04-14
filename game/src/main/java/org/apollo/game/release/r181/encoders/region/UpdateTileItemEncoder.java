package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.UpdateTileItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateTileItemMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class UpdateTileItemEncoder extends MessageEncoder<UpdateTileItemMessage> {

	@Override
	public GamePacket encode(UpdateTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(15);
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getAmount());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getPreviousAmount());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		return builder.toGamePacket();
	}

}