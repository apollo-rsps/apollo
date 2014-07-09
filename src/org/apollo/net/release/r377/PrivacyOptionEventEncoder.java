package org.apollo.net.release.r377;

import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link PrivacyOptionEvent}.
 * 
 * @author Major
 */
public final class PrivacyOptionEventEncoder extends EventEncoder<PrivacyOptionEvent> {

    @Override
    public GamePacket encode(final PrivacyOptionEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(201);

	builder.put(DataType.BYTE, event.getChatPrivacy().ordinal());
	builder.put(DataType.BYTE, event.getFriendPrivacy().ordinal());
	builder.put(DataType.BYTE, event.getTradePrivacy().ordinal());

	return builder.toGamePacket();
    }

}