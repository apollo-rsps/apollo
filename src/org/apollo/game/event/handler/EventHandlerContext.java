package org.apollo.game.event.handler;

import org.apollo.game.event.handler.chain.EventHandlerChain;

/**
 * Provides operations specific to an {@link EventHandler} in an
 * {@link EventHandlerChain}.
 * @author Graham
 */
public abstract class EventHandlerContext {

	/**
	 * Breaks the handler chain.
	 */
	public abstract void breakHandlerChain();

}
