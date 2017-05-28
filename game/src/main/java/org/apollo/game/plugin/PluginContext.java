package org.apollo.game.plugin;

import org.apollo.ServerContext;
import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainSet;
import org.apollo.net.message.Message;

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
		MessageHandlerChainSet chains = context.getGameService().getMessageHandlerChainSet();
		chains.putHandler(message, handler);
	}

}