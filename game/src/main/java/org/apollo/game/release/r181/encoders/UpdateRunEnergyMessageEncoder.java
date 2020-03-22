package org.apollo.game.release.r181.encoders;

import org.apollo.game.message.impl.encode.UpdateRunEnergyMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateRunEnergyMessage}.
 *
 * @author Khaled Abdeljaber
 */
public class UpdateRunEnergyMessageEncoder extends MessageEncoder<UpdateRunEnergyMessage> {
	@Override
	public GamePacket encode(UpdateRunEnergyMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(60, PacketType.FIXED);
		builder.put(DataType.BYTE, message.getEnergy());
		return builder.toGamePacket();
	}
}
