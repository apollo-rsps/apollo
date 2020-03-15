package org.apollo.game.message.handler;

import com.google.common.base.MoreObjects;
import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * A chain of {@link MessageHandler}s
 *
 * @param <M> The Message type this chain represents.
 * @author Graham
 * @author Ryley
 */
public final class MessageHandlerChain<M extends Message> {

	/**
	 * The List of MessageHandlers.
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
	 * @return {@code true} iff the Message propagated down the chain without being terminated.
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