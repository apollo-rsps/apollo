package org.apollo.net.session;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.game.GameConstants;
import org.apollo.game.GameService;
import org.apollo.game.event.Event;
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.event.handler.chain.EventHandlerChain;
import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.game.model.Player;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A game session.
 * @author Graham
 */
public final class GameSession extends Session {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GameSession.class.getName());

	/**
	 * The server context.
	 */
	private final ServerContext context;

	/**
	 * The queue of pending {@link Event}s.
	 */
	private final BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<Event>(GameConstants.EVENTS_PER_PULSE);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates a login session for the specified channel.
	 * @param channel The channel.
	 * @param context The server context.
	 * @param player The player.
	 */
	public GameSession(Channel channel, ServerContext context, Player player) {
		super(channel);
		this.context = context;
		this.player = player;
	}

	@Override
	public void messageReceived(Object message) throws Exception {
		Event event = (Event) message;
		if (eventQueue.size() >= GameConstants.EVENTS_PER_PULSE) {
			logger.warning("Too many events in queue for game session, dropping...");
		} else {
			eventQueue.add(event);
		}
	}

	/**
	 * Encodes and dispatches the specified event.
	 * @param event The event.
	 */
	public void dispatchEvent(Event event) {
		Channel channel = getChannel();
		if (channel.isBound() && channel.isConnected() && channel.isOpen()) {
			ChannelFuture future = channel.write(event);
			if (event.getClass() == LogoutEvent.class) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	/**
	 * Handles pending events for this session.
	 * @param chainGroup The event chain group.
	 */
	@SuppressWarnings("unchecked")
	public void handlePendingEvents(EventHandlerChainGroup chainGroup) {
		Event event;
		while ((event = eventQueue.poll()) != null) {
			// this lookup code really sucks!
			// TODO improve it!
			Class<? extends Event> eventType = event.getClass();
			EventHandlerChain<Event> chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);

			while (chain == null && eventType != null) {
				eventType = (Class<? extends Event>) eventType.getSuperclass();
				if (eventType == Event.class) {
					eventType = null;
				} else {
					chain = (EventHandlerChain<Event>) chainGroup.getChain(eventType);
				}
			}

			if (chain == null) {
				logger.warning("No chain for event: " + event.getClass().getName() + ".");
			} else {
				try {
					chain.handle(player, event);
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Error handling event.", ex);
				}
			}
		}
	}

	/**
	 * Handles a player saver response.
	 * @param success A flag indicating if the save was successful.
	 */
	public void handlePlayerSaverResponse(boolean success) {
		context.getService(GameService.class).finalizePlayerUnregistration(player);
	}

	@Override
	public void destroy() throws Exception {
		context.getService(GameService.class).unregisterPlayer(player);
	}

}
