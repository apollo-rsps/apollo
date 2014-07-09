package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.NpcActionEvent;
import org.apollo.game.model.World;
import org.apollo.game.model.WorldConstants;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;

/**
 * A verification {@link EventHandler} for the {@link NpcActionEvent}.
 *
 * @author Stuart
 */
public final class NpcActionVerificationHandler extends EventHandler<NpcActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, NpcActionEvent event) {
	if (event.getIndex() < 0 || event.getIndex() >= WorldConstants.MAXIMUM_NPCS) {
	    ctx.breakHandlerChain();
	    return;
	}

	Npc npc = World.getWorld().getNpcRepository().get(event.getIndex());

	if (npc == null || !player.getPosition().isWithinDistance(npc.getPosition(), 15)) {
	    ctx.breakHandlerChain();
	    return;
	}

	// TODO is this right?
	if (event.getOption() >= npc.getDefinition().getInteractions().length) {
	    ctx.breakHandlerChain();
	    return;
	}
    }

}