package org.apollo.game.release.r317;

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
 */
public final class SetUpdatedRegionMessageEncoder extends MessageEncoder<SetUpdatedRegionMessage> {

	@Override
	public GamePacket encode(SetUpdatedRegionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(85);
		Position player = message.getPlayerPosition(), region = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalY(player));
		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalX(player));

		return builder.toGamePacket();
	}

}