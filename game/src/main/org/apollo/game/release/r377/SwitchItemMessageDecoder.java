package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SwitchItemMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SwitchItemMessage}.
 *
 * @author Graham
 */
public final class SwitchItemMessageDecoder extends MessageDecoder<SwitchItemMessage> {

	@Override
	public SwitchItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int newSlot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		boolean inserting = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD) == 1;
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int oldSlot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new SwitchItemMessage(interfaceId, inserting, oldSlot, newSlot);
	}

}
