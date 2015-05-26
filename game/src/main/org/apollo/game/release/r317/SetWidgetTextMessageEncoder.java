package org.apollo.game.release.r317;

import org.apollo.game.message.impl.SetWidgetTextMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetWidgetTextMessage}.
 *
 * @author The Wanderer
 */
public final class SetWidgetTextMessageEncoder extends MessageEncoder<SetWidgetTextMessage> {

	@Override
	public GamePacket encode(SetWidgetTextMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(126, PacketType.VARIABLE_SHORT);
		builder.putString(message.getText());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getInterfaceId());

		return builder.toGamePacket();
	}

}