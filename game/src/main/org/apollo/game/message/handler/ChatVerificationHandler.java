package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ChatMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that verifies {@link ChatMessage}s.
 *
 * @author Graham
 */
public final class ChatVerificationHandler extends MessageHandler<ChatMessage> {

	/**
	 * Creates the ChatVerificationHandler.
	 *
	 * @param world The {@link World} the {@link ChatMessage} occurred in.
	 */
	public ChatVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ChatMessage message) {
		int color = message.getTextColor();
		int effects = message.getTextEffects();
		if (color < 0 || color > 11 || effects < 0 || effects > 5) {
			message.terminate();
		}
	}

}