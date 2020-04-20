package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.encode.FriendListUnlockMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class FriendListUnlockEncoder extends MessageEncoder<FriendListUnlockMessage> {
	@Override
	public GamePacket encode(FriendListUnlockMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(29, PacketType.FIXED);
		return builder.toGamePacket();
	}
}
