package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetInterfaceTextEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetInterfaceTextEvent}.
 * 
 * @author The Wanderer
 */
public final class SetInterfaceTextEventEncoder extends EventEncoder<SetInterfaceTextEvent> {

	@Override
	public GamePacket encode(SetInterfaceTextEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(126, PacketType.VARIABLE_SHORT);
		builder.putString(event.getText());
		builder.put(DataType.SHORT, DataTransformation.ADD, event.getInterfaceId());

		return builder.toGamePacket();
	}

}