package org.apollo.util.plugin;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.command.CommandListener;
import org.apollo.game.login.LoginListener;
import org.apollo.game.login.LogoutListener;
import org.apollo.game.message.Message;
import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainGroup;
import org.apollo.game.model.World;

/**
 * The {@link PluginContext} contains methods a plugin can use to interface with the server, for example, by adding
 * {@link MessageHandler}s to {@link MessageHandlerChain}s.
 * 
 * @author Graham
 */
public final class PluginContext {

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * Creates the plugin context.
	 * 
	 * @param context The server context.
	 */
	public PluginContext(ServerContext context) {
		this.context = context;
	}

	/**
	 * Adds a {@link CommandListener}.
	 * 
	 * @param name The name of the listener.
	 * @param listener The listener.
	 */
	public void addCommandListener(String name, CommandListener listener) {
		World.getWorld().getCommandDispatcher().register(name, listener);
	}

	/**
	 * Adds a {@link MessageHandler} to the end of the chain.
	 * 
	 * @param <M> The type of message.
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

	/**
	 * Adds a {@link LoginListener}.
	 * 
	 * @param listener The listener.
	 */
	public void addLoginListener(LoginListener listener) {
		World.getWorld().getLoginDispatcher().register(listener);
	}

	/**
	 * Adds a {@link LogoutListener}.
	 * 
	 * @param listener The listener.
	 */
	public void addLogoutListener(LogoutListener listener) {
		World.getWorld().getLogoutDispatcher().register(listener);
	}

}