package org.apollo.net.release.r317;

import org.apollo.game.message.impl.SectorChangeMessage;
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
		GamePacketBuilder builder = new GamePacketBuilder(73);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralSectorX());
		builder.put(DataType.SHORT, message.getPosition().getCentralSectorY());
		return builder.toGamePacket();
	}

}