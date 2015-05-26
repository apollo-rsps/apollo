package org.apollo.game.release.r317;

import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendObjectMessage}.
 *
 * @author Major
 */
public final class SendObjectMessageEncoder extends MessageEncoder<SendObjectMessage> {

	@Override
	public GamePacket encode(SendObjectMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(151);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getType() << 2 | message.getOrientation());
		return builder.toGamePacket();
	}

}