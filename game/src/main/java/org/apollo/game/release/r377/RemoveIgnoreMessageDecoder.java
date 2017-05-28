package org.apollo.game.release.r377;

import org.apollo.game.message.impl.RemoveIgnoreMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageDecoder} for the {@link RemoveIgnoreMessage}.
 *
 * @author Major
 */
public final class RemoveIgnoreMessageDecoder extends MessageDecoder<RemoveIgnoreMessage> {

	@Override
	public RemoveIgnoreMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new RemoveIgnoreMessage(username);
	}

}