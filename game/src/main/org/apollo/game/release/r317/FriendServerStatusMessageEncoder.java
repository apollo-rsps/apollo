package org.apollo.game.release.r317;

import org.apollo.game.message.impl.FriendServerStatusMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link FriendServerStatusMessage}.
 *
 * @author Major
 */
public final class FriendServerStatusMessageEncoder extends MessageEncoder<FriendServerStatusMessage> {

	@Override
	public GamePacket encode(FriendServerStatusMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(221);
		builder.put(DataType.BYTE, message.getStatusCode());
		return builder.toGamePacket();
	}

}