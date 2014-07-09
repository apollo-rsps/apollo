package org.apollo.net.release.r317;

import java.util.concurrent.atomic.AtomicInteger;

import org.apollo.game.event.impl.ForwardPrivateMessageEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventEncoder} for the {@link ForwardPrivateMessageEvent}.
 * 
 * @author Major
 */
public final class ForwardPrivateMessageEventEncoder extends EventEncoder<ForwardPrivateMessageEvent> {

    /**
     * The amount of messages sent globally, offset by a random variable x, {@code 0 <= x < 100,000,000}.
     */
    private static AtomicInteger messageCounter = new AtomicInteger((int) (Math.random() * 100_000_000));

    @Override
    public GamePacket encode(ForwardPrivateMessageEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(196, PacketType.VARIABLE_BYTE);

	builder.put(DataType.LONG, NameUtil.encodeBase37(event.getSenderUsername()));
	builder.put(DataType.INT, messageCounter.incrementAndGet());
	builder.put(DataType.BYTE, event.getSenderPrivilege().toInteger());
	builder.putBytes(event.getCompressedMessage());
	return builder.toGamePacket();
    }

}