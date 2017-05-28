package org.apollo.game.release.r317;

import org.apollo.game.message.impl.FlashTabInterfaceMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link FlashTabInterfaceMessage}.
 *
 * @author Major
 */
public final class FlashTabInterfaceMessageEncoder extends MessageEncoder<FlashTabInterfaceMessage> {

	@Override
	public GamePacket encode(FlashTabInterfaceMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(24);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getTab());
		return builder.toGamePacket();
	}

}