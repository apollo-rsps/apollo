package org.apollo.game.release.r181.decoders.social;

import org.apollo.game.message.impl.decode.PrivacyOptionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link PrivacyOptionMessage}.
 *
 * @author Kyle Stevenson
 */
public final class PrivacyOptionMessageDecoder extends MessageDecoder<PrivacyOptionMessage> {

	@Override
	public PrivacyOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int chatPrivacy = (int) reader.getUnsigned(DataType.BYTE);
		int friendPrivacy = (int) reader.getUnsigned(DataType.BYTE);
		int tradePrivacy = (int) reader.getUnsigned(DataType.BYTE);

		return new PrivacyOptionMessage(chatPrivacy, friendPrivacy, tradePrivacy);
	}

}