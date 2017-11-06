package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SendFriendMessage;
import org.apollo.game.message.impl.SendPlaySoundMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageEncoder} for the {@link SendPlaySoundMessage}.
 *
 * @author tlf30
 */
public final class SendPlaySoundMessageEncoder extends MessageEncoder<SendPlaySoundMessage> {

	@Override
	public GamePacket encode(SendPlaySoundMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(26);
		builder.put(DataType.SHORT, message.getId());
		builder.put(DataType.BYTE, message.getType());
		builder.put(DataType.SHORT, message.getDelay());
		return builder.toGamePacket();
	}

}