package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.IdleEvent;
import org.apollo.game.model.Player;

/**
 * An event handler for {@link org.apollo.game.event.impl.IdleEvent}
 *
 * Created by Stuart on 16/01/14.
 */
public class IdleEventHandler extends EventHandler<IdleEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, IdleEvent event) {

        if(player.getPrivilegeLevel() != Player.PrivilegeLevel.STANDARD) {
            return;
        }

        player.logout();
    }

}
