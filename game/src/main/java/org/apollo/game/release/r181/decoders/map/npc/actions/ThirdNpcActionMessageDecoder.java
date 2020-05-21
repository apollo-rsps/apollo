package org.apollo.game.release.r181.decoders.map.npc.actions;

import org.apollo.game.message.impl.decode.NpcActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the third {@link NpcActionMessage}.
 *
 * @author Khaled Abdeljaber
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