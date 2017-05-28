package org.apollo.game.release.r317;

import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link RegionChangeMessage}.
 *
 * @author Graham
 */
public final class RegionChangeMessageEncoder extends MessageEncoder<RegionChangeMessage> {

	@Override
	public GamePacket encode(RegionChangeMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(73);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
		builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
		return builder.toGamePacket();
	}

}