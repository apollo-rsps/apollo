package org.apollo.game.release.r317;

import org.apollo.game.message.impl.IdAssignmentMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link IdAssignmentMessage}.
 *
 * @author Graham
 */
public final class IdAssignmentMessageEncoder extends MessageEncoder<IdAssignmentMessage> {

	@Override
	public GamePacket encode(IdAssignmentMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(249);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.isMembers() ? 1 : 0);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}

}