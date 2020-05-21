package org.apollo.game.release.r377;

import org.apollo.game.message.impl.PrivateChatMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;
import org.apollo.util.TextUtil;

/**
 * A {@link MessageDecoder} for the {@link PrivateChatMessage}.
 *
 * @author Major
 */
public final class PrivateChatMessageDecoder extends MessageDecoder<PrivateChatMessage> {

	@Override
	public PrivateChatMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
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