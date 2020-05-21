package org.apollo.game.release.r377;

import org.apollo.game.message.impl.RemoveObjectMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link RemoveObjectMessage}.
 *
 * @author Major
 */
public final class RemoveObjectMessageEncoder extends MessageEncoder<RemoveObjectMessage> {

	@Override
	public GamePacket encode(RemoveObjectMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(88);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getPositionOffset());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getType() << 2 | message.getOrientation());
		return builder.toGamePacket();
	}

}