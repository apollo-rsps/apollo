package org.apollo.net.release.r377;

import io.netty.buffer.Unpooled;

import org.apollo.game.event.impl.EnterAmountEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link EnterAmountEvent}.
 * 
 * @author Graham
 */
public final class EnterAmountEventEncoder extends EventEncoder<EnterAmountEvent> {

    @Override
    public GamePacket encode(EnterAmountEvent event) {
	return new GamePacket(58, PacketType.FIXED, Unpooled.EMPTY_BUFFER);
    }

}