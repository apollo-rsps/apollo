package org.apollo.net.release.r377;

import org.apollo.game.event.impl.CharacterAnimationResetEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link CharacterAnimationResetEvent}.
 * 
 * @author Major
 */
public class CharacterAnimationResetEventEncoder extends EventEncoder<CharacterAnimationResetEvent> {

	@Override
	public GamePacket encode(CharacterAnimationResetEvent event) {
		return new GamePacketBuilder(13).toGamePacket();
	}

}