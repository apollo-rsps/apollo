package org.apollo.game.release.r181.decoders.map.obj;

import io.netty.buffer.ByteBufUtil;
import org.apollo.game.message.impl.decode.SpamPacketMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SpamPacketMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class SpamPacketMessageDecoder extends MessageDecoder<SpamPacketMessage> {

	@Override
	public SpamPacketMessage decode(GamePacket packet) {
		return new SpamPacketMessage(ByteBufUtil.getBytes(packet.content()));
	}

}