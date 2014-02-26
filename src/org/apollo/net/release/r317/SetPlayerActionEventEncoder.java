package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetPlayerActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * The {@link EventEncoder} for the {@link SetPlayerActionEvent}.
 * 
 * @author Major
 */
public final class SetPlayerActionEventEncoder extends EventEncoder<SetPlayerActionEvent> {

	@Override
	public GamePacket encode(SetPlayerActionEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(104);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getSlot());
		builder.put(DataType.BYTE, DataTransformation.ADD, event.isPrimaryAction() ? 0 : 1);
		builder.putString(event.getText());
		return builder.toGamePacket();
	}

}