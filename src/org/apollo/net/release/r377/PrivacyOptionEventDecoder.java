package org.apollo.net.release.r377;

import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link PrivacyOptionEvent}.
 * 
 * @author Major
 */
public final class PrivacyOptionEventDecoder extends EventDecoder<PrivacyOptionEvent> {

	@Override
	public PrivacyOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int publicChatState = (int) reader.getUnsigned(DataType.BYTE);
		int privateChatState = (int) reader.getUnsigned(DataType.BYTE);
		int tradeChatState = (int) reader.getUnsigned(DataType.BYTE);

		return new PrivacyOptionEvent(publicChatState, privateChatState, tradeChatState);
	}

}