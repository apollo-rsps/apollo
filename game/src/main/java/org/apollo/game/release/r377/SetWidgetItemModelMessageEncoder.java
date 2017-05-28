package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SetWidgetItemModelMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetWidgetItemModelMessage}.
 *
 * @author Chris Fletcher
 */
public final class SetWidgetItemModelMessageEncoder extends MessageEncoder<SetWidgetItemModelMessage> {

	@Override
	public GamePacket encode(SetWidgetItemModelMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(21);

		builder.put(DataType.SHORT, message.getZoom());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getModelId());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getInterfaceId());

		return builder.toGamePacket();
	}

}