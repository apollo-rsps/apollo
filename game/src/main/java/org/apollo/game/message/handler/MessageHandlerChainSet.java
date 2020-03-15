package org.apollo.game.message.handler;

import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * A group of {@link MessageHandlerChain}s classified by the {@link Message} type.
 *
 * @author Graham
 * @author Ryley
 * @author Major
 */
public final class MessageHandlerChainSet {

	/**
	 * The {@link Map} of {@link Message} {@link Class} types to {@link MessageHandlerChain}s
	 */
	private final Map<Class<? extends Message>, MessageHandlerChain<? extends Message>> chains = new HashMap<>();

	/**
	 * The {@link Map} of {@link Message} {@link Class} types to {@link Deque}s of all Classes in the chain.
	 */
	private final Map<Class<? extends Message>, Deque<Class<? extends Message>>> classes = new HashMap<>();

	/**
	 * Notifies the appropriate {@link MessageHandlerChain} that a {@link Message} has been received.
	 *
	 * @param player The {@link Player} receiving the Message.
	 * @param message The Message.
	 * @return {@code true} iff the Message propagated down the chain without being terminated.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Message> boolean notify(Player player, M message) {
		Deque<Class<? extends Message>> classes = this.classes.computeIfAbsent(message.getClass(),
				this::getMessageClasses);

		for (Class<? extends Message> type : classes) {
			MessageHandlerChain<? super M> chain = (MessageHandlerChain<? super M>) chains.get(type);

			if (chain != null && !chain.notify(player, message)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Places the {@link MessageHandlerChain} into this set.
	 *
	 * @param clazz The {@link Class} to associate the MessageHandlerChain with.
	 * @param handler The MessageHandlerChain.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Message> void putHandler(Class<M> clazz, MessageHandler<? extends Message> handler) {
		MessageHandlerChain<M> chain = (MessageHandlerChain<M>) chains.computeIfAbsent(clazz, MessageHandlerChain::new);
		chain.addHandler((MessageHandler<M>) handler);
	}

	/**
	 * Gets the {@link Deque} of {@link Class}es that can be handled.
	 *
	 * @param type The Class type of the message. Must not be the Class for {@link Message} itself.
	 * @return The Deque of Classes. Will never be {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private <M extends Message> Deque<Class<? extends Message>> getMessageClasses(Class<M> type) {
		Deque<Class<? extends Message>> classes = new ArrayDeque<>();
		Class<? super M> clazz = type;

		do {
			classes.addFirst((Class<? extends Message>) clazz);
			clazz = clazz.getSuperclass();
		} while (clazz != Message.class);

		return classes;
	}

}