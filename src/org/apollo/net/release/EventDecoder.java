package org.apollo.net.release;

import org.apollo.game.event.Event;
import org.apollo.net.codec.game.GamePacket;

/**
 * An {@link EventDecoder} decodes a {@link GamePacket} into an {@link Event}
 * object which can be processed by the server.
 * @author Graham
 * @param <E> The type of {@link Event}.
 */
public abstract class EventDecoder<E extends Event> {

	/**
	 * Decodes the specified packet into an event.
	 * @param packet The packet.
	 * @return The event.
	 */
	public abstract E decode(GamePacket packet);

}
