package org.apollo.game.message.handler;

import java.util.Map;

import org.apollo.game.message.Message;

/**
 * A group of {@link MessageHandlerChain}s classified by the {@link Message} type.
 * 
 * @author Graham
 */
public final class MessageHandlerChainGroup {

	/**
	 * The map of message classes to message handler chains.
	 */
	private final Map<Class<? extends Message>, MessageHandlerChain<?>> chains;

	/**
	 * Creates the message handler chain group.
	 * 
	 * @param chains The chains map.
	 */
	public MessageHandlerChainGroup(Map<Class<? extends Message>, MessageHandlerChain<?>> chains) {
		this.chains = chains;
	}

	/**
	 * Gets a {@link MessageHandlerChain} from this group.
	 * 
	 * @param <M> The type of message.
	 * @param clazz The message class.
	 * @return The {@link MessageHandlerChain} if one was found, {@code null} otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Message> MessageHandlerChain<M> getChain(Class<M> clazz) {
		return (MessageHandlerChain<M>) chains.get(clazz);
	}

	/**
	 * Registers a {@link MessageHandlerChain} associated with the specified {@link Class} to this group.
	 * 
	 * @param clazz The message class.
	 * @param chain The message handler chain.
	 */
	public <M extends Message> void register(Class<M> clazz, MessageHandlerChain<M> chain) {
		chains.put(clazz, chain);
	}

}