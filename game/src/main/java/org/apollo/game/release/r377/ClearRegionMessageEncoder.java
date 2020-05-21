package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ClearRegionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link ClearRegionMessage}.
 *
 * @author Major
 */
public final class ClearRegionMessageEncoder extends MessageEncoder<ClearRegionMessage> {

	@Override
	public GamePacket encode(ClearRegionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(40);
		Position player = message.getPlayerPosition(), region = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, region.getLocalY(player));
		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalX(player));

		return builder.toGamePacket();
	}

}