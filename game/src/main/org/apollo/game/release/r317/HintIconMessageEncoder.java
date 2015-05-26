package org.apollo.game.release.r317;

import org.apollo.game.message.impl.HintIconMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link HintIconMessage}.
 *
 * @author Major
 */
public final class HintIconMessageEncoder extends MessageEncoder<HintIconMessage> {

	@Override
	public GamePacket encode(HintIconMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(254);
		HintIconMessage.Type type = message.getType();
		builder.put(DataType.BYTE, type.getValue());

		switch (type) {
			case NPC:
			case PLAYER:
				builder.put(DataType.SHORT, message.getIndex());
				builder.put(DataType.TRI_BYTE, 0); // Dummy bytes
				break;
			default:
				throw new IllegalStateException("Unsupported hint icon type " + type + ".");
		}

		return builder.toGamePacket();
	}

}