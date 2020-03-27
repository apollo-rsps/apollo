package org.apollo.game.release.r181.decoders.ui.if3;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class If3ActionMessageDecoder extends MessageDecoder<IfActionMessage> {

	private final int action;

	public If3ActionMessageDecoder(int action) {
		this.action = action;
	}

	@Override
	public IfActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int packedInterface = (int) reader.getUnsigned(DataType.INT);
		int slot = (int) reader.getUnsigned(DataType.SHORT);
		int item = (int) reader.getUnsigned(DataType.SHORT);

		return new IfActionMessage(action, packedInterface >> 16, packedInterface & 0xFFFF, slot, item);
	}
}
