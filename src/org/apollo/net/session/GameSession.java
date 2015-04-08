package org.apollo.net.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.ServerContext;
import org.apollo.game.GameConstants;
import org.apollo.game.GameService;
import org.apollo.game.message.Message;
import org.apollo.game.message.MessageHandlerChainSet;
import org.apollo.game.message.impl.LogoutMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.util.CollectionUtil;

/**
 * A game session.
 *
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
	 * The queue of pending {@link Message}s.
	 */
	private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(GameConstants.MESSAGES_PER_PULSE);

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates a login session for the specified channel.
	 *
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
	public void destroy() {
		context.getService(GameService.class).unregisterPlayer(player);
	}

	/**
	 * Encodes and dispatches the specified message.
	 *
	 * @param message The message.
	 */
	public void dispatchMessage(Message message) {
		Channel channel = getChannel();
		if (channel.isActive() && channel.isOpen()) {
			ChannelFuture future = channel.writeAndFlush(message);
			if (message.getClass() == LogoutMessage.class) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	/**
	 * Handles pending messages for this session.
	 *
	 * @param chainSet The {@link MessageHandlerChainSet}
	 */
	public void handlePendingMessages(MessageHandlerChainSet chainSet) {
		CollectionUtil.pollAll(messageQueue, message -> {
			try {
				chainSet.notify(player, message);
			} catch (Exception reason) {
				logger.log(Level.SEVERE, "Uncaught exception thrown while handling message: " + message, reason);
			}
		});
	}

	/**
	 * Handles a player saver response.
	 *
	 * @param success A flag indicating if the save was successful.
	 */
	public void handlePlayerSaverResponse(boolean success) {
		context.getService(GameService.class).finalizePlayerUnregistration(player);
	}

	@Override
	public void messageReceived(Object message) {
		if (messageQueue.size() >= GameConstants.MESSAGES_PER_PULSE) {
			logger.warning("Too many messages in queue for game session, dropping...");
		} else {
			messageQueue.add((Message) message);
		}
	}

}