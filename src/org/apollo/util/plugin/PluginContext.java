package org.apollo.util.plugin;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.message.Message;
import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainGroup;

/**
 * The {@link PluginContext} contains methods a plugin can use to interface with the server, for example, by adding
 * {@link MessageHandler}s to {@link MessageHandlerChain}s.
 * 
 * @author Graham
 * @author Major
 */
public final class PluginContext {

	/**
	 * The ServerContext.
	 */
	private final ServerContext context;

	/**
	 * Creates the PluginContext.
	 * 
	 * @param context The {@link ServerContext}.
	 */
	public PluginContext(ServerContext context) {
		this.context = context;
	}

	/**
	 * Adds a {@link MessageHandler} to the end of the chain.
	 * 
	 * @param message The message.
	 * @param handler The handler.
	 */
	public <M extends Message> void addLastMessageHandler(Class<M> message, MessageHandler<M> handler) {
		MessageHandlerChainGroup chains = context.getService(GameService.class).getMessageHandlerChains();
		MessageHandlerChain<M> chain = chains.getChain(message);
		if (chain == null) {
			chain = new MessageHandlerChain<>(handler);
			chains.register(message, chain);
		} else {
			chain.addLast(handler);
		}
	}

}