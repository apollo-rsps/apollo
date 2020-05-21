package org.apollo.game.release.r377;

import org.apollo.game.message.impl.PrivacyOptionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link PrivacyOptionMessage}.
 *
 * @author Major
 */
public final class PrivacyOptionMessageDecoder extends MessageDecoder<PrivacyOptionMessage> {

	@Override
	public PrivacyOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int publicChatState = (int) reader.getUnsigned(DataType.BYTE);
		int privateChatState = (int) reader.getUnsigned(DataType.BYTE);
		int tradeChatState = (int) reader.getUnsigned(DataType.BYTE);

		return new PrivacyOptionMessage(publicChatState, privateChatState, tradeChatState);
	}

}