package org.apollo.game.release.r181.decoders.map.npc;

import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the {@link NpcActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class SecondNpcActionMessageDecoder extends MessageDecoder<NpcActionMessage> {

	@Override
	public NpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int index = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
		int movementType = (int) reader.getSigned(DataType.BYTE);

		return new NpcActionMessage(2, index, movementType);
	}

}