package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SetUpdatedRegionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetUpdatedRegionMessage}.
 *
 * @author Chris Fletcher
 * @author Major
 */
public final class SetUpdatedRegionMessageEncoder extends MessageEncoder<SetUpdatedRegionMessage> {

	@Override
	public GamePacket encode(SetUpdatedRegionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(75);
		Position base = message.getPlayerPosition(), position = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, position.getLocalX(base));
		builder.put(DataType.BYTE, DataTransformation.ADD, position.getLocalY(base));

		return builder.toGamePacket();
	}

}