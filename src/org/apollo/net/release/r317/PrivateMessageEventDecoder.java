package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivateMessageEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;
import org.apollo.util.TextUtil;

/**
 * An {@link EventDecoder} for the {@link PrivateMessageEvent}.
 * 
 * @author Major
 */
public final class PrivateMessageEventDecoder extends EventDecoder<PrivateMessageEvent> {

    @Override
    public PrivateMessageEvent decode(GamePacket packet) {
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

	return new PrivateMessageEvent(username, new String(uncompressed), recompressed);
    }

}