package org.apollo.game.release.r377;

import org.apollo.game.message.impl.UpdateTileItemMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateTileItemMessage}.
 *
 * @author Major
 */
public final class UpdateTileItemMessageEncoder extends MessageEncoder<UpdateTileItemMessage> {

	@Override
	public GamePacket encode(UpdateTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(121);
		builder.put(DataType.BYTE, message.getPositionOffset());
		builder.put(DataType.SHORT, message.getId());
		builder.put(DataType.SHORT, message.getPreviousAmount());
		builder.put(DataType.SHORT, message.getAmount());
		return builder.toGamePacket();
	}

}