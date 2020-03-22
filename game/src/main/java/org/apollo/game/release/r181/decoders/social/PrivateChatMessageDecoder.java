package org.apollo.game.release.r181.decoders.social;

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
		int length = packet.getLength() - Long.BYTES;

		byte[] originalCompressed = new byte[length];
		reader.getBytes(originalCompressed);

		String decompressed = TextUtil.decompress(originalCompressed, length);
		decompressed = TextUtil.filterInvalidCharacters(decompressed);
		decompressed = TextUtil.capitalize(decompressed);

		byte[] recompressed = new byte[length];
		TextUtil.compress(decompressed, recompressed);

		return new PrivateChatMessage(decompressed, recompressed, username);
	}

}