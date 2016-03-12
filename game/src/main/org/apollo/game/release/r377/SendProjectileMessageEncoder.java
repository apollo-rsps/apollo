package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SendProjectileMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Projectile;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendProjectileMessage}.
 */
public final class SendProjectileMessageEncoder extends MessageEncoder<SendProjectileMessage> {

	@Override
	public GamePacket encode(SendProjectileMessage message) {
		Projectile projectile = message.getProjectile();
		Position source = projectile.getPosition();
		Position destination = projectile.getDestination();

		GamePacketBuilder builder = new GamePacketBuilder(181);
		builder.put(DataType.BYTE, message.getPositionOffset());
		builder.put(DataType.BYTE, destination.getX() - source.getX());
		builder.put(DataType.BYTE, destination.getY() - source.getY());
		builder.put(DataType.SHORT, projectile.getTarget());
		builder.put(DataType.SHORT, projectile.getGraphic());
		builder.put(DataType.BYTE, projectile.getStartHeight());
		builder.put(DataType.BYTE, projectile.getEndHeight());
		builder.put(DataType.SHORT, projectile.getDelay());
		builder.put(DataType.SHORT, projectile.getLifetime());
		builder.put(DataType.BYTE, projectile.getPitch());
		builder.put(DataType.BYTE, projectile.getOffset());
		return builder.toGamePacket();
	}

}
