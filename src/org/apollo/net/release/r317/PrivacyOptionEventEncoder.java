package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link PrivacyOptionEvent}.
 * 
 * @author Kyle Stevenson
 */
public final class PrivacyOptionEventEncoder extends EventEncoder<PrivacyOptionEvent> {

	@Override
	public GamePacket encode(final PrivacyOptionEvent event) {
		final GamePacketBuilder builder = new GamePacketBuilder(206);

		builder.put(DataType.BYTE, event.getChatPrivacy().toInteger());
		builder.put(DataType.BYTE, event.getFriendPrivacy().toInteger());
		builder.put(DataType.BYTE, event.getTradePrivacy().toInteger());

		return builder.toGamePacket();
	}

}