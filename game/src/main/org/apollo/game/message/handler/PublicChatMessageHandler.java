package org.apollo.game.message.handler;

import org.apollo.game.message.impl.PublicChatMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * A {@link MessageHandler} that broadcasts public chat messages.
 *
 * @author Graham
 */
public final class PublicChatMessageHandler extends MessageHandler<PublicChatMessage> {

	/**
	 * Creates the PublicChatMessageHandler.
	 *
	 * @param world The {@link World} the {@link PublicChatMessage} occurred in.
	 */
	public PublicChatMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, PublicChatMessage message) {
		if (player.isMuted()) {
			message.terminate();
			return;
		}
		player.getBlockSet().add(SynchronizationBlock.createChatBlock(player, message));
	}

}