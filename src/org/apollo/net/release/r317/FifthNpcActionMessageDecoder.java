package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FifthNpcActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link org.apollo.net.release.MessageDecoder} for the {@link org.apollo.game.message.impl.FifthNpcActionMessage}.
 *
 * @author Stuart
 */
public final class FifthNpcActionMessageDecoder extends MessageDecoder<FifthNpcActionMessage> {

	@Override
	public FifthNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		return new FifthNpcActionMessage(index);
	}

}