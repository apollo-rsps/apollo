package org.apollo.game.release.r377;

import org.apollo.game.message.impl.FifthNpcActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FifthNpcActionMessage}.
 *
 * @author Stuart
 */
public final class FifthNpcActionMessageDecoder extends MessageDecoder<FifthNpcActionMessage> {

	@Override
	public FifthNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new FifthNpcActionMessage(index);
	}

}