package org.apollo.game.message.handler;

import org.apollo.game.message.impl.PublicChatMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that verifies {@link PublicChatMessage}s.
 *
 * @author Graham
 */
public final class PublicChatVerificationHandler extends MessageHandler<PublicChatMessage> {

	/**
	 * Creates the PublicChatVerificationHandler.
	 *
	 * @param world The {@link World} the {@link PublicChatMessage} occurred in.
	 */
	public PublicChatVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, PublicChatMessage message) {
		int color = message.getTextColor();
		int effects = message.getTextEffects();
		if (color < 0 || color > 11 || effects < 0 || effects > 5) {
			message.terminate();
		}
	}

}