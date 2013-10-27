package org.apollo.net.release.r377;

import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link RegionChangeEvent}.
 * @author Graham
 */
public final class RegionChangeEventEncoder extends EventEncoder<RegionChangeEvent> {

	@Override
	public GamePacket encode(RegionChangeEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(222);
		builder.put(DataType.SHORT, event.getPosition().getCentralRegionY());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getPosition().getCentralRegionX());
		return builder.toGamePacket();
	}

}
