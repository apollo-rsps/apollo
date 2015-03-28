package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.PlayerActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.util.MobRepository;

/**
 * A verification {@link MessageHandler} for the {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class PlayerActionVerificationHandler extends MessageHandler<PlayerActionMessage> {

	/**
	 * Creates the PlayerActionVerificationHandler.
	 *
	 * @param world The {@link World} the {@link PlayerActionMessage} occurred in.
	 */
	public PlayerActionVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(MessageHandlerContext ctx, Player player, PlayerActionMessage message) {
		int index = message.getIndex();
		MobRepository<Player> repository = world.getPlayerRepository();

		if (index < 0 || index >= repository.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Player other = repository.get(index);
		if (other == null || !player.getPosition().isWithinDistance(other.getPosition(), player.getViewingDistance() + 1)) {
			// +1 in case it was decremented after the player clicked the action.
			ctx.breakHandlerChain();
			return;
		}
	}

}