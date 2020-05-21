package org.apollo.game.release.r181.decoders.social;

import org.apollo.cache.def.HuffmanCodec;
import org.apollo.game.message.impl.decode.PublicChatMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.TextUtil;

/**
 * @author Khaled Abdeljaber
 */
public class PublicChatDecoder extends MessageDecoder<PublicChatMessage> {
	@Override
	public PublicChatMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		final var type = PublicChatMessage.PublicChatType.of((int) reader.getUnsigned(DataType.BYTE));
		final var color = (int) reader.getUnsigned(DataType.BYTE);
		final var effects = (int) reader.getUnsigned(DataType.BYTE);

		final var originalCompressed = new byte[packet.getLength() - 3];
		reader.getBytes(originalCompressed);

		String uncompressed = HuffmanCodec.decompress(originalCompressed);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		final var recompressed = HuffmanCodec.compress(uncompressed); // in case invalid data gets sent, this effectively verifies it
		return new PublicChatMessage(uncompressed, recompressed, color, effects, type);
	}
}
