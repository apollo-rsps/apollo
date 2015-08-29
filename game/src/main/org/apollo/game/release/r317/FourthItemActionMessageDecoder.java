package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the fourth {@link ItemActionMessage}.
 *
 * @author Graham
 */
public final class FourthItemActionMessageDecoder extends MessageDecoder<ItemActionMessage> {

	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new ItemActionMessage(4, interfaceId, id, slot);
	}

}