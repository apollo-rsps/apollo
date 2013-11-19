package org.apollo.net.release.r377;

import org.apollo.game.event.impl.PositionEvent;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link PositionEvent}.
 * 
 * @author Chris Fletcher
 * @author Major
 */
final class PositionEventEncoder extends EventEncoder<PositionEvent> {

	@Override
	public GamePacket encode(PositionEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(75);
		Position base = event.getBase(), pos = event.getPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, pos.getLocalX(base));
		builder.put(DataType.BYTE, DataTransformation.ADD, pos.getLocalY(base));

		return builder.toGamePacket();
	}

}