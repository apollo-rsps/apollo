package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetWidgetVisibilityEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetVisibilityEvent}.
 * 
 * @author Major
 */
final class SetWidgetVisibilityEventEncoder extends EventEncoder<SetWidgetVisibilityEvent> {

	@Override
	public GamePacket encode(SetWidgetVisibilityEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(171);

		builder.put(DataType.BYTE, event.isVisible() ? 0 : 1);
		builder.put(DataType.SHORT, event.getWidgetId());

		return builder.toGamePacket();
	}

}