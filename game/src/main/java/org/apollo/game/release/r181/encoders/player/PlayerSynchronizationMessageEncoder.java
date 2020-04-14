package org.apollo.game.release.r181.encoders.player;

import org.apollo.game.message.impl.encode.PlayerSynchronizationMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PlayerSynchronizationMessage}.
 *
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationMessageEncoder extends MessageEncoder<PlayerSynchronizationMessage> {

	@Override
	public GamePacket encode(PlayerSynchronizationMessage message) {
		return message.getInfo().toGamePacket();
	}
}