package org.apollo.net.release.r317;

import org.apollo.game.event.impl.DisplayCrossbonesEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link DisplayCrossbonesEvent}.
 * 
 * @author Major
 */
public final class DisplayCrossbonesEventEncoder extends EventEncoder<DisplayCrossbonesEvent> {

	@Override
	public GamePacket encode(DisplayCrossbonesEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(61);
		builder.put(DataType.BYTE, event.isDisplayed() ? 1 : 0);
		return builder.toGamePacket();
	}

}