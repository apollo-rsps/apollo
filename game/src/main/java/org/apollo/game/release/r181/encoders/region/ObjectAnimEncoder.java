package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.ObjectAnimationMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class ObjectAnimEncoder extends MessageEncoder<ObjectAnimationMessage> {

	@Override
	public GamePacket encode(ObjectAnimationMessage message) {
		final var animation = message.getAnimation();
		final var object = message.getObject();
		final var builder = new GamePacketBuilder(49, PacketType.FIXED);

		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, object.getType() << 2 | object.getOrientation());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getPositionOffset());
		builder.put(DataType.SHORT, animation.getId());
		return builder.toGamePacket();
	}
}
