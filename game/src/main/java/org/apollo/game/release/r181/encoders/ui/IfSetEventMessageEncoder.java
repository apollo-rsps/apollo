package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetEventMessage;
import org.apollo.game.model.inter.InterfaceEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetEventMessageEncoder extends MessageEncoder<IfSetEventMessage> {
	@Override
	public GamePacket encode(IfSetEventMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(48, PacketType.FIXED);

		builder.put(DataType.INT, DataOrder.LITTLE, message.getEvents().stream().mapToInt(InterfaceEvent::getMask).sum());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getRange().getEndInclusive());
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());
		builder.put(DataType.SHORT, message.getRange().getStart());

		return builder.toGamePacket();
	}
}
