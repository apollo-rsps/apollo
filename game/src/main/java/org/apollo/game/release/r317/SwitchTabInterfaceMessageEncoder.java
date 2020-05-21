package org.apollo.game.release.r317;

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
		GamePacketBuilder builder = new GamePacketBuilder(71);
		builder.put(DataType.SHORT, message.getInterfaceId());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getTabId());
		return builder.toGamePacket();
	}

}