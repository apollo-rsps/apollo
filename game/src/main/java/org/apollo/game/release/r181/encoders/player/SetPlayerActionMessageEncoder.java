package org.apollo.game.release.r181.encoders.player;

import org.apollo.game.message.impl.encode.SetPlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class SetPlayerActionMessageEncoder extends MessageEncoder<SetPlayerActionMessage> {
	@Override
	public GamePacket encode(SetPlayerActionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(66, PacketType.VARIABLE_BYTE);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.isPrimaryAction() ? 0 : 1);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getSlot());
		builder.putString(message.getText());
		return builder.toGamePacket();
	}
}
