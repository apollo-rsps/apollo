package org.apollo.game.model.entity.event;

import java.util.ArrayList;
import java.util.List;

/**
 * A chain of {@link EventListener}s.
 *
 * @author Major
 * @param <E> The type of {@link Event} the listeners in this chain listen for.
 */
final class EventListenerChain<E extends Event> {

	/**
	 * The List of EventListeners.
	 */
	private final List<EventListener<E>> listeners = new ArrayList<>();

	/**
	 * Adds an {@link EventListener} to this chain.
	 * 
	 * @param listener The EventListener to add.
	 */
	public void addListener(EventListener<E> listener) {
		listeners.add(listener);
	}

	/**
	 * Notifies each {@link EventListener} in this chain that an {@link Event} has occurred.
	 * 
	 * @param event The event.
	 * @return {@code true} if the Event should continue on with its outcome, {@code false} if not.
	 */
	public boolean notify(E event) {
		EventContext context = new EventContext();
		for (EventListener<E> listener : listeners) {
			listener.handle(event, context);

			if (context.terminated()) {
				return false;
			}
		}

		return false;
	}

}