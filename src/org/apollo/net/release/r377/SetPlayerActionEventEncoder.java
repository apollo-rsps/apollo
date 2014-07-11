package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SetPlayerActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * The {@link EventEncoder} for the {@link SetPlayerActionEvent}.
 * 
 * @author Major
 */
public final class SetPlayerActionEventEncoder extends EventEncoder<SetPlayerActionEvent> {

	@Override
	public GamePacket encode(SetPlayerActionEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(157, PacketType.VARIABLE_BYTE);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getSlot());
		builder.putString(event.getText());
		builder.put(DataType.BYTE, event.isPrimaryAction() ? 0 : 1);
		return builder.toGamePacket();
	}

}