package org.apollo.net.release.r317;

import org.apollo.game.event.impl.IdleEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link org.apollo.net.release.EventDecoder} for the {@link org.apollo.game.event.impl.IdleEvent}
 *
 * Created by Stuart on 16/01/14.
 */
public class IdleEventDecoder extends EventDecoder<IdleEvent> {

    @Override
    public IdleEvent decode(GamePacket packet) {
        return new IdleEvent();
    }

}
