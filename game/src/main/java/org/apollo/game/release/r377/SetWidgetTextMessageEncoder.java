package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SetWidgetTextMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetWidgetTextMessage}.
 *
 * @author Graham
 */
public final class SetWidgetTextMessageEncoder extends MessageEncoder<SetWidgetTextMessage> {

	@Override
	public GamePacket encode(SetWidgetTextMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(232, PacketType.VARIABLE_SHORT);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getInterfaceId());
		builder.putString(message.getText());
		return builder.toGamePacket();
	}

}
