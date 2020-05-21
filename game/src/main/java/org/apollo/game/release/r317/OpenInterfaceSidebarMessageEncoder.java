package org.apollo.game.release.r317;

import org.apollo.game.message.impl.OpenInterfaceSidebarMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link OpenInterfaceSidebarMessage}.
 *
 * @author Graham
 */
public final class OpenInterfaceSidebarMessageEncoder extends MessageEncoder<OpenInterfaceSidebarMessage> {

	@Override
	public GamePacket encode(OpenInterfaceSidebarMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(248);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getInterfaceId());
		builder.put(DataType.SHORT, message.getSidebarId());
		return builder.toGamePacket();
	}

}