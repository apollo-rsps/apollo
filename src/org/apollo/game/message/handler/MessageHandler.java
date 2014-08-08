package org.apollo.game.message.handler;

import org.apollo.game.message.Message;
import org.apollo.game.model.entity.Player;

/**
 * Handles messages received from the client.
 * 
 * @author Graham
 * @param <M> The type of message handled by this class.
 */
public abstract class MessageHandler<M extends Message> {

	/**
	 * Handles a message.
	 * 
	 * @param ctx The context.
	 * @param player The player.
	 * @param message The message.
	 */
	public abstract void handle(MessageHandlerContext ctx, Player player, M message);

}