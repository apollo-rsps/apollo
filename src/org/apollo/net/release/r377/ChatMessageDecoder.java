package org.apollo.net.release.r377;

import org.apollo.game.message.impl.ChatMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.TextUtil;

/**
 * A {@link MessageDecoder} for the {@link ChatMessage}.
 * 
 * @author Graham
 */
public final class ChatMessageDecoder extends MessageDecoder<ChatMessage> {

	@Override
	public ChatMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int color = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE);
		int effects = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);

		int length = packet.getLength() - 2;

		byte[] originalCompressed = new byte[length];
		reader.getBytes(originalCompressed);

		String uncompressed = TextUtil.uncompress(originalCompressed, length);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		byte[] recompressed = new byte[length];
		TextUtil.compress(uncompressed, recompressed); // in case invalid data gets sent, this effectively verifies it

		return new ChatMessage(uncompressed, recompressed, color, effects);
	}

}