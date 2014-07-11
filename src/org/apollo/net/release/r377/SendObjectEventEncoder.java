package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SendObjectEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SendObjectEvent}.
 * 
 * @author Major
 */
public final class SendObjectEventEncoder extends EventEncoder<SendObjectEvent> {

	@Override
	public GamePacket encode(SendObjectEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(152);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getType() << 2 + event.getOrientation());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getId());
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
		return builder.toGamePacket();
	}

}