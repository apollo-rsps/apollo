package org.apollo.game.model.entity.event;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of {@link EventListenerChain}s.
 *
 * @author Major
 */
public final class EventListenerChainSet {

	/**
	 * The Map of Event Classes to EventListenerChains.
	 */
	private final Map<Class<? extends Event>, EventListenerChain<? extends Event>> chains = new HashMap<>();

	/**
	 * Notifies the appropriate {@link EventListenerChain} that an {@link Event} has occurred.
	 * 
	 * @param event The Event.
	 * @return {@code true} if the Event should continue on with its outcome, {@code false} if not.
	 */
	public <E extends Event> boolean notify(E event) {
		@SuppressWarnings("unchecked")
		EventListenerChain<E> chain = (EventListenerChain<E>) chains.get(event);
		return chain.notify(event);
	}

	/**
	 * Places the {@link EventListenerChain} into this set.
	 * 
	 * @param clazz The {@link Class} to associate the EventListenerChain with.
	 * @param chain The EventListenerChain.
	 */
	public <E extends Event> void putListenerChain(Class<E> clazz, EventListenerChain<E> chain) {
		chains.put(clazz, chain);
	}

}