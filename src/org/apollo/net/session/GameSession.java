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
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainGroup;
import org.apollo.game.message.impl.LogoutMessage;
import org.apollo.game.model.entity.Player;

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
	 * @param chainGroup The message chain group.
	 */
	@SuppressWarnings("unchecked")
	public void handlePendingMessages(MessageHandlerChainGroup chainGroup) {
		Message message;
		while ((message = messageQueue.poll()) != null) {
			// this lookup code really sucks!
			// TODO improve it!
			Class<? extends Message> messageType = message.getClass();
			MessageHandlerChain<Message> chain = (MessageHandlerChain<Message>) chainGroup.getChain(messageType);

			while (chain == null && messageType != null) {
				messageType = (Class<? extends Message>) messageType.getSuperclass();
				if (messageType == Message.class) {
					messageType = null;
				} else {
					chain = (MessageHandlerChain<Message>) chainGroup.getChain(messageType);
				}
			}

			if (chain != null) {
				try {
					chain.handle(player, message);
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Error handling message: ", ex);
				}
			}
		}
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