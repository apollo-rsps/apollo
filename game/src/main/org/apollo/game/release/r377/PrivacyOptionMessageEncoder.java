package org.apollo.game.release.r377;

import org.apollo.game.message.impl.PrivacyOptionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PrivacyOptionMessage}.
 *
 * @author Major
 */
public final class PrivacyOptionMessageEncoder extends MessageEncoder<PrivacyOptionMessage> {

	@Override
	public GamePacket encode(final PrivacyOptionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(201);

		builder.put(DataType.BYTE, message.getChatPrivacy().ordinal());
		builder.put(DataType.BYTE, message.getFriendPrivacy().ordinal());
		builder.put(DataType.BYTE, message.getTradePrivacy().ordinal());

		return builder.toGamePacket();
	}

}