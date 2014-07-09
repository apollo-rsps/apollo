package org.apollo.net.release.r377;

import org.apollo.game.event.impl.MobAnimationResetEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link MobAnimationResetEvent}.
 * 
 * @author Major
 */
public final class MobAnimationResetEventEncoder extends EventEncoder<MobAnimationResetEvent> {

    @Override
    public GamePacket encode(MobAnimationResetEvent event) {
	return new GamePacketBuilder(13).toGamePacket();
    }

}