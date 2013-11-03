package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SetWidgetTextEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetTextEvent}.
 * 
 * @author Graham
 */
public final class SetWidgetTextEventEncoder extends EventEncoder<SetWidgetTextEvent> {

	@Override
	public GamePacket encode(SetWidgetTextEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(232, PacketType.VARIABLE_SHORT);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());
		builder.putString(event.getText());
		return builder.toGamePacket();
	}

}
