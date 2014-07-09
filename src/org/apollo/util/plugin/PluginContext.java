package org.apollo.util.plugin;

import org.apollo.ServerContext;
import org.apollo.game.GameService;
import org.apollo.game.command.CommandListener;
import org.apollo.game.event.Event;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.chain.EventHandlerChain;
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.login.LoginListener;
import org.apollo.game.login.LogoutListener;
import org.apollo.game.model.World;
import org.apollo.net.release.EventDecoder;
import org.apollo.net.release.EventEncoder;
import org.apollo.net.release.Release;

/**
 * The {@link PluginContext} contains methods a plugin can use to interface with the server, for example, by adding
 * {@link EventHandler}s to {@link EventHandlerChain}s.
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
     * Adds an {@link EventDecoder}.
     * 
     * @param <T> The type of decoder.
     * @param releaseNo The release number.
     * @param opcode The opcode.
     * @param decoder The event decoder.
     */
    public <T extends Event> void addEventDecoder(int releaseNo, int opcode, EventDecoder<T> decoder) {
	Release release = context.getRelease();
	if (release.getReleaseNumber() == releaseNo) {
	    release.register(opcode, decoder);
	}
    }

    /**
     * Adds an {@link EventEncoder}.
     * 
     * @param <E> The type of encoder.
     * @param releaseNo The release number.
     * @param event The event.
     * @param encoder The event encoder.
     */
    public <E extends Event> void addEventEncoder(int releaseNo, Class<E> event, EventEncoder<E> encoder) {
	Release release = context.getRelease();
	if (release.getReleaseNumber() == releaseNo) {
	    release.register(event, encoder);
	}
    }

    /**
     * Adds an {@link EventHandler} to the end of the chain.
     * 
     * @param <E> The type of event.
     * @param event The event.
     * @param handler The handler.
     */
    public <E extends Event> void addLastEventHandler(Class<E> event, EventHandler<E> handler) {
	EventHandlerChainGroup chains = context.getService(GameService.class).getEventHandlerChains();
	EventHandlerChain<E> chain = chains.getChain(event);
	if (chain == null) {
	    chain = new EventHandlerChain<>(handler);
	    chains.register(event, chain);
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