package org.apollo.game.message;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.entity.Player;

import com.google.common.base.MoreObjects;

/**
 * A chain of {@link MessageHandler}s
 * 
 * @author Graham
 * @author Ryley
 * @param <M> The Message type this chain represents.
 */
public final class MessageHandlerChain<M extends Message> {

	/**
	 * The handlers.
	 */
	private final List<MessageHandler<M>> handlers = new ArrayList<>();

	/**
	 * The Class type of this chain.
	 */
	private final Class<M> type;

	/**
	 * Constructs a new {@link MessageHandlerChain}.
	 * 
	 * @param type The Class type of this chain.
	 */
	public MessageHandlerChain(Class<M> type) {
		this.type = type;
	}

	/**
	 * Adds the specified {@link MessageHandler} to this chain.
	 * 
	 * @param handler The MessageHandler.
	 */
	public void addHandler(MessageHandler<M> handler) {
		handlers.add(handler);
	}

	/**
	 * Notifies each {@link MessageHandler} in this chain that a {@link Message} has been received.
	 * 
	 * @param player The Player to handle this message for.
	 * @param message The Message.
	 * @return {@code true} if and only if the Message propagated down the chain without being terminated, otherwise
	 *         {@code false}.
	 */
	public boolean notify(Player player, M message) {
		for (MessageHandler<M> handler : handlers) {
			handler.handle(player, message);

			if (message.terminated()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", type).add("handlers", handlers).toString();
	}

}