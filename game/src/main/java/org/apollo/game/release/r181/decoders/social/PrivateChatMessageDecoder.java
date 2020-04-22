package org.apollo.game.release.r181.decoders.social;

import org.apollo.cache.def.HuffmanCodec;
import org.apollo.game.message.impl.PrivateChatMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.TextUtil;

/**
 * A {@link MessageDecoder} for the {@link PrivateChatMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class PrivateChatMessageDecoder extends MessageDecoder<PrivateChatMessage> {

	@Override
	public PrivateChatMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		String username = reader.getString();

		final var originalCompressed = new byte[reader.getLength()];
		reader.getBytes(originalCompressed);

		String decompressed = HuffmanCodec.decompress(originalCompressed);
		decompressed = TextUtil.filterInvalidCharacters(decompressed);
		decompressed = TextUtil.capitalize(decompressed);

		final var recompressed = HuffmanCodec.compress(decompressed);
		return new PrivateChatMessage(decompressed, recompressed, username);
	}

}