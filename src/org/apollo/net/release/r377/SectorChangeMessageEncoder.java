package org.apollo.net.release.r377;

import org.apollo.game.message.impl.SectorChangeMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SectorChangeMessage}.
 * 
 * @author Graham
 */
public final class SectorChangeMessageEncoder extends MessageEncoder<SectorChangeMessage> {

	@Override
	public GamePacket encode(SectorChangeMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(222);
		builder.put(DataType.SHORT, message.getPosition().getCentralSectorY());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getPosition().getCentralSectorX());
		return builder.toGamePacket();
	}

}