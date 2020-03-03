package org.apollo.game.release.r181.decoders.npc;

import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the third {@link NpcActionMessage}.
 *
 * @author Major
 */
public final class ThirdNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int movementType = (int) reader.getSigned(DataType.BYTE);

		return new NpcActionMessage(3, index, movementType);
	}

}