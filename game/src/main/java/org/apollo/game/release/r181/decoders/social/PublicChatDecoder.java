package org.apollo.game.release.r181.decoders.social;

import org.apollo.game.message.impl.PublicChatMessage;
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

		final var length = packet.getLength() - 3;

		final var originalCompressed = new byte[length];
		reader.getBytes(originalCompressed);

		String uncompressed = TextUtil.decompress(originalCompressed, length);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		final var recompressed = new byte[length];
		TextUtil.compress(uncompressed, recompressed); // in case invalid data gets sent, this effectively verifies it

		return new PublicChatMessage(uncompressed, recompressed, color, effects, type);
	}
}
