package org.apollo.game.release.r377;

import org.apollo.game.message.impl.OpenSidebarMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link OpenSidebarMessage}.
 *
 * @author Major
 */
public final class OpenSidebarMessageEncoder extends MessageEncoder<OpenSidebarMessage> {

	@Override
	public GamePacket encode(OpenSidebarMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(246);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getSidebarId());
		return builder.toGamePacket();
	}

}