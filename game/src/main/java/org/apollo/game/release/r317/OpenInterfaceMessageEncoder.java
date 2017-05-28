package org.apollo.game.release.r317;

import org.apollo.game.message.impl.OpenInterfaceMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link OpenInterfaceMessage}.
 *
 * @author Graham
 */
public final class OpenInterfaceMessageEncoder extends MessageEncoder<OpenInterfaceMessage> {

	@Override
	public GamePacket encode(OpenInterfaceMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(97);
		builder.put(DataType.SHORT, message.getId());
		return builder.toGamePacket();
	}

}