package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SetWidgetPlayerModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetPlayerModelEvent}.
 * 
 * @author Chris Fletcher
 */
final class SetWidgetPlayerModelEventEncoder extends EventEncoder<SetWidgetPlayerModelEvent> {

	@Override
	public GamePacket encode(SetWidgetPlayerModelEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(255);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());
		return builder.toGamePacket();
	}

}