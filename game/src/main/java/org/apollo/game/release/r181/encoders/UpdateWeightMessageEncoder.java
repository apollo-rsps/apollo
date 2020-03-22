package org.apollo.game.release.r181.encoders;

import org.apollo.game.message.impl.encode.UpdateWeightMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateWeightMessage}.
 *
 * @author Khaled Abdeljaber
 */
public class UpdateWeightMessageEncoder extends MessageEncoder<UpdateWeightMessage> {
	@Override
	public GamePacket encode(UpdateWeightMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(71, PacketType.FIXED);
		builder.put(DataType.SHORT, message.getWeight());
		return builder.toGamePacket();
	}
}
