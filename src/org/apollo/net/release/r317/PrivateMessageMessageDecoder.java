package org.apollo.net.release.r317;

import org.apollo.game.message.impl.PrivateMessageMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;
import org.apollo.util.TextUtil;

/**
 * A {@link MessageDecoder} for the {@link PrivateMessageMessage}.
 * 
 * @author Major
 */
public final class PrivateMessageMessageDecoder extends MessageDecoder<PrivateMessageMessage> {

	@Override
	public PrivateMessageMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		int length = packet.getLength() - 8;

		byte[] originalCompressed = new byte[length];
		reader.getBytes(originalCompressed);

		String uncompressed = TextUtil.uncompress(originalCompressed, length);
		uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
		uncompressed = TextUtil.capitalize(uncompressed);

		byte[] recompressed = new byte[length];
		TextUtil.compress(uncompressed, recompressed);

		return new PrivateMessageMessage(username, new String(uncompressed), recompressed);
	}

}