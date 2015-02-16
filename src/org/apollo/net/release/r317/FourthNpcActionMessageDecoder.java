package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FourthNpcActionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link org.apollo.net.release.MessageDecoder} for the {@link org.apollo.game.message.impl.FourthNpcActionMessage}.
 *
 * @author Stuart
 */
public final class FourthNpcActionMessageDecoder extends MessageDecoder<FourthNpcActionMessage> {

	@Override
	public FourthNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT);
		return new FourthNpcActionMessage(index);
	}

}