package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.NpcActionEvent;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.util.MobRepository;

/**
 * A verification {@link EventHandler} for the {@link NpcActionEvent}.
 *
 * @author Stuart
 * @author Major
 */
public final class NpcActionVerificationHandler extends EventHandler<NpcActionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, NpcActionEvent event) {
		MobRepository<Npc> repository = World.getWorld().getNpcRepository();
		int index = event.getIndex();

		if (index < 0 || index >= repository.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Npc npc = repository.get(index);

		if (npc == null || !player.getPosition().isWithinDistance(npc.getPosition(), 15)) {
			ctx.breakHandlerChain();
			return;
		}

		if (event.getOption() >= npc.getDefinition().getInteractions().length) {
			ctx.breakHandlerChain();
			return;
		}
	}

}