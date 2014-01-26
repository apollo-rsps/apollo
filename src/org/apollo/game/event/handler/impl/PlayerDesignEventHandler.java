package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.CloseInterfaceEvent;
import org.apollo.game.event.impl.PlayerDesignEvent;
import org.apollo.game.model.Player;

/**
 * A handler which handles {@link PlayerDesignEvent}s.
 * 
 * @author Graham
 */
public final class PlayerDesignEventHandler extends EventHandler<PlayerDesignEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PlayerDesignEvent event) {
		player.setAppearance(event.getAppearance());
		player.setDesigned(true);
		player.send(new CloseInterfaceEvent());
	}

}