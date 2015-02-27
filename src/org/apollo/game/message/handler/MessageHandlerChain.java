package org.apollo.game.message.handler;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import org.apollo.game.message.Message;
import org.apollo.game.model.entity.Player;

/**
 * A chain of message handlers.
 * 
 * @author Graham
 * @author Ryley
 * @param <M> The type of message handled by this chain.
 */
public final class MessageHandlerChain<M extends Message> {

	/**
	 * The handlers.
	 */
	private final Deque<MessageHandler<M>> handlers;

	/**
	 * Creates the message handler chain.
	 * 
	 * @param handlers The handlers.
	 */
	@SafeVarargs
	public MessageHandlerChain(MessageHandler<M>... handlers) {
		this.handlers = new ArrayDeque<>(Arrays.asList(handlers));
	}

	/**
	 * Dynamically adds a message handler to the end of the chain.
	 * 
	 * @param handler The handler.
	 */
	public void addLast(MessageHandler<M> handler) {
		handlers.addLast(handler);
	}

	/**
	 * Handles the message, passing it down the chain until the chain is broken
	 * or the message reaches the end of the chain.
	 * 
	 * @param player The player.
	 * @param message The message.
	 */
	public void handle(Player player, M message) {
		MessageHandlerContext context = new MessageHandlerContext();

		for (MessageHandler<M> handler : handlers) {
			handler.handle(context, player, message);

			if (context.isBroken()) {
				break;
			}
		}
	}

}