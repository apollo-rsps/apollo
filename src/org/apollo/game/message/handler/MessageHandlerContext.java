package org.apollo.game.message.handler;

/**
 * Provides operations specific to a {@link MessageHandler} in a {@link MessageHandlerChain}.
 * 
 * @author Graham
 */
public abstract class MessageHandlerContext {

	/**
	 * Breaks the handler chain.
	 */
	public abstract void breakHandlerChain();

}