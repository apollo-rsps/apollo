package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.DialogueContinueEvent;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.InterfaceType;

/**
 * An {@link EventHandler} for the {@link DialogueContinueEvent}.
 * 
 * @author Chris Fletcher
 */
public final class DialogueContinueEventHandler extends EventHandler<DialogueContinueEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, DialogueContinueEvent event) {
		if (player.getInterfaceSet().contains(InterfaceType.DIALOGUE)) {
			player.getInterfaceSet().continueRequested();
		}
	}

}