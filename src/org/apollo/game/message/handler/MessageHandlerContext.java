package org.apollo.game.message.handler;

/**
 * Provides operations specific to a {@link MessageHandler} in a
 * {@link MessageHandlerChain}.
 * 
 * @author Graham
 * @author Ryley
 */
public final class MessageHandlerContext {

	/**
	 * Denotes whether or not this handler chain is broken.
	 */
	private boolean broken;

	/**
	 * Breaks the handler chain.
	 */
	public void breakHandlerChain() {
		broken = true;
	}

	/**
	 * Returns whether or not this handler chain is broken.
	 * 
	 * @return {@code true} if this handler chain is broken, otherwise
	 *         {@code false}.
	 */
	public boolean isBroken() {
		return broken;
	}

}