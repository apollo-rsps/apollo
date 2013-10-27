package org.apollo.net.release.r377;

import org.apollo.game.event.impl.ClosedInterfaceEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ClosedInterfaceEvent}.
 * @author Graham
 */
public final class ClosedInterfaceEventDecoder extends EventDecoder<ClosedInterfaceEvent> {

	@Override
	public ClosedInterfaceEvent decode(GamePacket packet) {
		return new ClosedInterfaceEvent();
	}

}
