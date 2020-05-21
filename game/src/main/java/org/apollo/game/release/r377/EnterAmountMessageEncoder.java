package org.apollo.game.release.r377;

import io.netty.buffer.Unpooled;

import org.apollo.game.message.impl.EnterAmountMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link EnterAmountMessage}.
 *
 * @author Graham
 */
public final class EnterAmountMessageEncoder extends MessageEncoder<EnterAmountMessage> {

	@Override
	public GamePacket encode(EnterAmountMessage message) {
		return new GamePacket(58, PacketType.FIXED, Unpooled.EMPTY_BUFFER);
	}

}