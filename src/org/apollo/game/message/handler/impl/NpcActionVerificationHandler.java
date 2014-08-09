package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.NpcActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.util.MobRepository;

/**
 * A verification {@link MessageHandler} for the {@link NpcActionMessage}.
 *
 * @author Stuart
 * @author Major
 */
public final class NpcActionVerificationHandler extends MessageHandler<NpcActionMessage> {

	/**
	 * The world's npc repository.
	 */
	private final MobRepository<Npc> repository = World.getWorld().getNpcRepository();

	@Override
	public void handle(MessageHandlerContext ctx, Player player, NpcActionMessage message) {
		int index = message.getIndex();

		if (index < 0 || index >= repository.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Npc npc = repository.get(index);

		if (npc == null || !player.getPosition().isWithinDistance(npc.getPosition(), player.getViewingDistance() + 1)) {
			// +1 in case it was decremented after the player clicked the action.
			ctx.breakHandlerChain();
			return;
		}

		NpcDefinition definition = npc.getDefinition();
		if (message.getOption() >= definition.getInteractions().length) {
			ctx.breakHandlerChain();
			return;
		}
	}

}