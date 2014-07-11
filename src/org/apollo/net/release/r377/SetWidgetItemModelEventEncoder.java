package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SetWidgetItemModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetItemModelEvent}.
 * 
 * @author Chris Fletcher
 */
final class SetWidgetItemModelEventEncoder extends EventEncoder<SetWidgetItemModelEvent> {

	@Override
	public GamePacket encode(SetWidgetItemModelEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(21);

		builder.put(DataType.SHORT, event.getZoom());
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getModelId());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());

		return builder.toGamePacket();
	}

}