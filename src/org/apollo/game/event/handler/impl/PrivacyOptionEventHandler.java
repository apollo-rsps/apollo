package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.game.model.Player;

/**
 * Handles {@link PrivacyOptionEvent} from the client to the server.
 *
 * @author Kyle Stevenson
 *         Date: 12/24/13
 *         Time: 2:03 AM
 */
public class PrivacyOptionEventHandler extends EventHandler<PrivacyOptionEvent> {
    @Override
    public void handle(final EventHandlerContext ctx, final Player player, final PrivacyOptionEvent event) {
        player.setPrivacyPublicChat(event.getPrivacyPublicChat());
        player.setPrivacyPrivateChat(event.getPrivacyPrivateChat());
        player.setPrivacyTradeCompete(event.getPrivacyTradeCompete());
    }
}
