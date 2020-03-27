package org.apollo.game.release.r181.decoders.map.npc.actions;

import org.apollo.game.message.impl.decode.NpcActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the first {@link NpcActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FirstNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int movementType = (int) reader.getSigned(DataType.BYTE, DataTransformation.NEGATE);
		int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		return new NpcActionMessage(1, index, movementType);
	}

}