package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SetPlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * The {@link MessageEncoder} for the {@link SetPlayerActionMessage}.
 *
 * @author Major
 */
public final class SetPlayerActionMessageEncoder extends MessageEncoder<SetPlayerActionMessage> {

	@Override
	public GamePacket encode(SetPlayerActionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(157, PacketType.VARIABLE_BYTE);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getSlot());
		builder.putString(message.getText());
		builder.put(DataType.BYTE, message.isPrimaryAction() ? 0 : 1);
		return builder.toGamePacket();
	}

}