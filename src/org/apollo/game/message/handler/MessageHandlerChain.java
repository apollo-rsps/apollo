package org.apollo.game.message.handler;

import org.apollo.game.message.Message;
import org.apollo.game.model.entity.Player;

/**
 * A chain of message handlers.
 * 
 * @author Graham
 * @param <M> The type of message handled by this chain.
 */
public final class MessageHandlerChain<M extends Message> {

	/**
	 * The handlers.
	 */
	private MessageHandler<M>[] handlers;

	/**
	 * Creates the message handler chain.
	 * 
	 * @param handlers The handlers.
	 */
	@SafeVarargs
	public MessageHandlerChain(MessageHandler<M>... handlers) {
		this.handlers = handlers;
	}

	/**
	 * Dynamically adds a message handler to the end of the chain.
	 * 
	 * @param handler The handler.
	 */
	@SuppressWarnings("unchecked")
	public void addLast(MessageHandler<M> handler) {
		MessageHandler<M>[] old = handlers;
		handlers = new MessageHandler[old.length + 1];
		System.arraycopy(old, 0, handlers, 0, old.length);
		handlers[old.length] = handler;
	}

	/**
	 * Handles the message, passing it down the chain until the chain is broken or the message reaches the end of the
	 * chain.
	 * 
	 * @param player The player.
	 * @param message The message.
	 */
	public void handle(Player player, M message) {
		final boolean[] running = new boolean[1];
		running[0] = true;

		MessageHandlerContext ctx = new MessageHandlerContext() {

			@Override
			public void breakHandlerChain() {
				running[0] = false;
			}
		};

		for (MessageHandler<M> handler : handlers) {
			handler.handle(ctx, player, message);
			if (!running[0]) {
				break;
			}
		}
	}

}