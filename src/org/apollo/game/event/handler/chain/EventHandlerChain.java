package org.apollo.game.event.handler.chain;

import org.apollo.game.event.Event;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.model.Player;

/**
 * A chain of event handlers.
 * @author Graham
 * @param <E> The type of event the handlers in this chain handle.
 */
public final class EventHandlerChain<E extends Event> {

	/**
	 * The handlers.
	 */
	private EventHandler<E>[] handlers;

	/**
	 * Creates the event handler chain.
	 * @param handlers The handlers.
	 */
	@SafeVarargs
	public EventHandlerChain(EventHandler<E>... handlers) {
		this.handlers = handlers;
	}

	/**
	 * Dynamically adds an event handler to the end of the chain.
	 * @param handler The handler.
	 */
	@SuppressWarnings("unchecked")
	public void addLast(EventHandler<E> handler) {
		EventHandler<E>[] old = handlers;
		handlers = new EventHandler[old.length + 1];
		System.arraycopy(old, 0, handlers, 0, old.length);
		handlers[old.length] = handler;
	}

	/**
	 * Handles the event, passing it down the chain until the chain is broken
	 * or the event reaches the end of the chain.
	 * @param player The player.
	 * @param event The event.
	 */
	public void handle(Player player, E event) {
		final boolean[] running = new boolean[1];
		running[0] = true;

		EventHandlerContext ctx = new EventHandlerContext() {

			@Override
			public void breakHandlerChain() {
				running[0] = false;
			}

		};

		for (EventHandler<E> handler : handlers) {
			handler.handle(ctx, player, event);
			if (!running[0]) {
				break;
			}
		}
	}

}
