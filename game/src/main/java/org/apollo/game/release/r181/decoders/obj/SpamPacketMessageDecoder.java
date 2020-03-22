package org.apollo.game.release.r181.decoders.obj;

import io.netty.buffer.ByteBufUtil;
import org.apollo.game.message.impl.SpamPacketMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SpamPacketMessage}.
 *
 * @author Major
 */
public final class SpamPacketMessageDecoder extends MessageDecoder<SpamPacketMessage> {

	@Override
	public SpamPacketMessage decode(GamePacket packet) {
		return new SpamPacketMessage(ByteBufUtil.getBytes(packet.content()));
	}

}