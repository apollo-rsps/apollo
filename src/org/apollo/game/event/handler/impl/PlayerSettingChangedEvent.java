package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.Player;

/**
 * An {@link org.apollo.game.event.handler.EventHandler} which responds to {@link org.apollo.game.event.impl.ButtonEvent}s for changing a players setting, ie brightness, chat effects etc etc
 *
 * Created by Stuart on 16/01/14.
 */
public class PlayerSettingChangedEvent extends EventHandler<ButtonEvent> {

    private static final int DARK_BRIGHTNESS = 906;
    private static final int NORMAL_BRIGHTNESS = 908;
    private static final int BRIGHT_BRIGHTNESS = 910;
    private static final int VERY_BRIGHT_BRIGHTNESS = 912;

    @Override
    public void handle(EventHandlerContext ctx, Player player, ButtonEvent event) {
        switch(event.getWidgetId()) {
            case DARK_BRIGHTNESS:
                player.setScreenBrightness((byte)1);
                break;
            case NORMAL_BRIGHTNESS:
                player.setScreenBrightness((byte)2);
                break;
            case BRIGHT_BRIGHTNESS:
                player.setScreenBrightness((byte)3);
                break;
            case VERY_BRIGHT_BRIGHTNESS:
                player.setScreenBrightness((byte)4);
                break;
        }
    }

}
