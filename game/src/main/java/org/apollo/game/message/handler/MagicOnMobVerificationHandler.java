package org.apollo.game.message.handler;

import org.apollo.game.message.impl.MagicOnMobMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Mob;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Player;

/**
 * A verification {@link MessageHandler} for the {@link MagicOnMobMessage}.
 *
 * @author Tom
 */
public final class MagicOnMobVerificationHandler extends MessageHandler<MagicOnMobMessage>{

	/**
	 * Creates the MessageListener.
	 *
	 * @param world The {@link World} the {@link MagicOnMobMessage} occurred in.
	 */
	public MagicOnMobVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, MagicOnMobMessage message) {
		int index = message.getIndex();
		MobRepository<? extends Mob> repository;

		if (message.getType() == EntityType.NPC) {
			repository = world.getNpcRepository();
		} else if (message.getType() == EntityType.PLAYER) {
			repository = world.getPlayerRepository();
		} else {
			throw new IllegalStateException("Invalid mob type for message: " + message.toString());
		}

		if (index < 0 || index >= repository.capacity()) {
			message.terminate();
			return;
		}

		Mob mob = repository.get(index);

		if (mob == null || !player.getPosition().isWithinDistance(mob.getPosition(), player.getViewingDistance() + 1)) {
			// +1 in case it was decremented after the player clicked the action.
			message.terminate();
		}
	}
}
