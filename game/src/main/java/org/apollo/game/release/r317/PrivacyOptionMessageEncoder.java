package org.apollo.game.release.r317;

import org.apollo.game.message.impl.PrivacyOptionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PrivacyOptionMessage}.
 *
 * @author Kyle Stevenson
 */
public final class PrivacyOptionMessageEncoder extends MessageEncoder<PrivacyOptionMessage> {

	@Override
	public GamePacket encode(final PrivacyOptionMessage message) {
		final GamePacketBuilder builder = new GamePacketBuilder(206);

		builder.put(DataType.BYTE, message.getChatPrivacy().toInteger(true));
		builder.put(DataType.BYTE, message.getFriendPrivacy().toInteger(true));
		builder.put(DataType.BYTE, message.getTradePrivacy().toInteger(true));

		return builder.toGamePacket();
	}

}