package org.apollo.game.release.r377;

import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the first {@link NpcActionMessage}.
 *
 * @author Major
 */
public final class FirstNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		return new NpcActionMessage(1, index);
	}

}