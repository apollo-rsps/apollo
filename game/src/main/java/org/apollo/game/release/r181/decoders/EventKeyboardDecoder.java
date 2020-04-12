package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.EventKeyboardMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

import java.time.Duration;

/**
 * @author Khaled Abdeljaber
 */
public class EventKeyboardDecoder extends MessageDecoder<EventKeyboardMessage> {
	@Override
	public EventKeyboardMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		final var events = new EventKeyboardMessage.EventKeyboard[reader.getLength() / 4];
		for (int index = 0; index < events.length; index++) {
			var keyPressed = reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE);
			var duration = reader.getUnsigned(DataType.TRI_BYTE);
			events[index] = new EventKeyboardMessage.EventKeyboard((int) keyPressed, Duration.ofMillis(duration));
		}

		return new EventKeyboardMessage(events);
	}
}
