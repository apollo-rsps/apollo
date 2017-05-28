package org.apollo.game.release.r317;

import org.apollo.game.message.impl.RemoveTileItemMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link RemoveTileItemMessage}.
 *
 * @author Major
 */
public final class RemoveTileItemMessageEncoder extends MessageEncoder<RemoveTileItemMessage> {

	@Override
	public GamePacket encode(RemoveTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(156);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.SHORT, message.getId());
		return builder.toGamePacket();
	}

}