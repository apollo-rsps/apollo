package org.apollo.game.event.handler.chain;

import java.util.Map;

import org.apollo.game.event.Event;

/**
 * A group of {@link EventHandlerChain}s classified by the {@link Event} type.
 * @author Graham
 */
public final class EventHandlerChainGroup {

	/**
	 * The map of event classes to event handler chains.
	 */
	private final Map<Class<? extends Event>, EventHandlerChain<?>> chains;

	/**
	 * Creates the event handler chain group.
	 * @param chains The chains map.
	 */
	public EventHandlerChainGroup(Map<Class<? extends Event>, EventHandlerChain<?>> chains) {
		this.chains = chains;
	}

	/**
	 * Gets an {@link EventHandlerChain} from this group.
	 * @param <E> The type of event.
	 * @param clazz The event class.
	 * @return The {@link EventHandlerChain} if one was found, {@code null}
	 * otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Event> EventHandlerChain<E> getChain(Class<E> clazz) {
		return (EventHandlerChain<E>) chains.get(clazz);
	}

}
