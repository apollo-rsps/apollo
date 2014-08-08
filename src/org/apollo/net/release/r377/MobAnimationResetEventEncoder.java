package org.apollo.net.release.r377;

import org.apollo.game.message.impl.MobAnimationResetMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link MobAnimationResetMessage}.
 * 
 * @author Major
 */
public final class MobAnimationResetEventEncoder extends MessageEncoder<MobAnimationResetMessage> {

	@Override
	public GamePacket encode(MobAnimationResetMessage message) {
		return new GamePacketBuilder(13).toGamePacket();
	}

}