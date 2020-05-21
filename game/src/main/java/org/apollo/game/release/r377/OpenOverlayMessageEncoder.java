package org.apollo.game.release.r377;

import org.apollo.game.message.impl.OpenOverlayMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link OpenOverlayMessage}.
 *
 * @author Major
 */
public final class OpenOverlayMessageEncoder extends MessageEncoder<OpenOverlayMessage> {

	@Override
	public GamePacket encode(OpenOverlayMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(50);
		builder.put(DataType.SHORT, message.getOverlayId());
		return builder.toGamePacket();
	}

}