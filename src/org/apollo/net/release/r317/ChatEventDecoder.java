package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.TextUtil;

/**
 * An {@link EventDecoder} for the {@link ChatEvent}.
 * @author Graham
 */
public final class ChatEventDecoder extends EventDecoder<ChatEvent> {

	@Override
	public ChatEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int effects = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int color = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int length = packet.getLength() - 2;

		byte[] originalCompressed = new byte[length];
		reader.getBytesReverse(DataTransformation.ADD, originalCompressed);

		String uncompressed = TextUtil.uncompress(originalCompressed, length);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		byte[] recompressed = new byte[length];
		TextUtil.compress(uncompressed, recompressed); // in case invalid data gets sent, this effectively verifies it

		return new ChatEvent(uncompressed, recompressed, color, effects);
	}

}
