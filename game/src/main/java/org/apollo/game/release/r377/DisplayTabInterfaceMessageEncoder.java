package org.apollo.game.release.r377;

import org.apollo.game.message.impl.DisplayTabInterfaceMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link DisplayTabInterfaceMessage}.
 *
 * @author Chris Fletcher
 */
public final class DisplayTabInterfaceMessageEncoder extends MessageEncoder<DisplayTabInterfaceMessage> {

	@Override
	public GamePacket encode(DisplayTabInterfaceMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(252);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getTab());
		return builder.toGamePacket();
	}

}