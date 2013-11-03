package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SpamPacketEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link SpamPacketEvent}.
 * 
 * @author Major
 */
public class SpamPacketEventDecoder extends EventDecoder<SpamPacketEvent> {

	@Override
	public SpamPacketEvent decode(GamePacket packet) {
		return new SpamPacketEvent(packet.getPayload().array());
	}

}