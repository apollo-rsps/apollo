package org.apollo.game.release.r377;

import org.apollo.game.message.impl.UpdateRunEnergyMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateRunEnergyMessage}
 *
 * @author Major
 */
public final class UpdateRunEnergyMessageEncoder extends MessageEncoder<UpdateRunEnergyMessage> {

	@Override
	public GamePacket encode(UpdateRunEnergyMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(125);
		builder.put(DataType.BYTE, message.getEnergy());
		return builder.toGamePacket();
	}

}