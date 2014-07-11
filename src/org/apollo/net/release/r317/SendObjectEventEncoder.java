package org.apollo.net.release.r317;

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
		GamePacketBuilder builder = new GamePacketBuilder(151);
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, event.getType() << 2 + event.getOrientation());
		return builder.toGamePacket();
	}

}