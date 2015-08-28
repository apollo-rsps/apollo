package org.apollo.game.release.r317;

import org.apollo.game.message.impl.SetWidgetItemModelMessage;
import org.apollo.net.codec.game.DataOrder;
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
		GamePacketBuilder builder = new GamePacketBuilder(246);

		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getInterfaceId());
		builder.put(DataType.SHORT, message.getZoom());
		builder.put(DataType.SHORT, message.getModelId());

		return builder.toGamePacket();
	}

}
