package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SwitchTabInterfaceMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SwitchTabInterfaceMessage}.
 *
 * @author Graham
 */
public final class SwitchTabInterfaceMessageEncoder extends MessageEncoder<SwitchTabInterfaceMessage> {

	@Override
	public GamePacket encode(SwitchTabInterfaceMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(10);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getTabId());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getInterfaceId());
		return builder.toGamePacket();
	}

}
