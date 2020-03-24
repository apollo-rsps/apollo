package org.apollo.game.release.r181.decoders.ui.opheld;

import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class SixthOpHeldMessageDecoder extends MessageDecoder<ItemActionMessage> {
	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int itemId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new ItemActionMessage(6, -1, -1, itemId, -1);
	}
}
