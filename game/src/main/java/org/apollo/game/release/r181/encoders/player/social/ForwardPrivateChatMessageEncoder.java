package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.ForwardPrivateChatMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link MessageEncoder} for the {@link ForwardPrivateChatMessage}.
 *
 * @author Major
 */
public final class ForwardPrivateChatMessageEncoder extends MessageEncoder<ForwardPrivateChatMessage> {

	/**
	 * The amount of messages sent globally, offset by a random variable x, {@code 0 <= x < 100,000,000}.
	 */
	private static AtomicInteger messageCounter = new AtomicInteger((int) (Math.random() * 100_000_000));

	@Override
	public GamePacket encode(ForwardPrivateChatMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(10, PacketType.VARIABLE_SHORT);

		builder.putString(message.getSenderUsername());
		long count = messageCounter.incrementAndGet();
		builder.put(DataType.SHORT, count >> 16);
		builder.put(DataType.TRI_BYTE, count & 0xFFFFFF);
		builder.put(DataType.BYTE, message.getSenderPrivilege().toInteger());

		final var compressed = message.getCompressedMessage();
		builder.putSmart(compressed.length);
		builder.putBytes(compressed);

		return builder.toGamePacket();
	}

}