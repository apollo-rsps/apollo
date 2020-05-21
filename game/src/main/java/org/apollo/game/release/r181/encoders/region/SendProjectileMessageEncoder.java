package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.SendProjectileMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Projectile;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 * A {@link MessageEncoder} for the {@link SendProjectileMessage}.
 */
public final class SendProjectileMessageEncoder extends MessageEncoder<SendProjectileMessage> {

	@Override
	public GamePacket encode(SendProjectileMessage message) {
		Projectile projectile = message.getProjectile();
		Position source = projectile.getPosition();
		Position destination = projectile.getDestination();

		GamePacketBuilder builder = new GamePacketBuilder(11);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, projectile.getEndHeight());
		builder.put(DataType.SHORT, projectile.getLifetime());
		builder.put(DataType.SHORT, DataTransformation.ADD, projectile.getGraphic());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, projectile.getStartHeight());
		builder.put(DataType.SHORT, DataOrder.LITTLE, projectile.getTarget());
		builder.put(DataType.BYTE, projectile.getPitch());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, projectile.getOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, projectile.getDelay());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.BYTE, DataTransformation.ADD, destination.getY() - source.getY());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, destination.getX() - source.getX());

		return builder.toGamePacket();
	}

}
