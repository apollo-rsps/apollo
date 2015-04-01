package org.apollo.util.plugin;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.message.Message;
import org.apollo.game.message.MessageHandler;
import org.apollo.game.message.MessageHandlerChain;
import org.apollo.game.message.MessageHandlerChainSet;

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
	 * Adds a {@link MessageHandler} to the {@link MessageHandlerChainSet}.
	 * 
	 * @param message The message.
	 * @param handler The handler.
	 */
	public <M extends Message> void addMessageHandler(Class<M> message, MessageHandler<M> handler) {
		MessageHandlerChainSet chains = context.getService(GameService.class).getMessageHandlerChainSet();
		chains.putHandler(message, handler);
	}

}