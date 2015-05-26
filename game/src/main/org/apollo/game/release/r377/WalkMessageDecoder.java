package org.apollo.game.release.r377;

import org.apollo.game.message.impl.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link WalkMessage}.
 *
 * @author Graham
 */
public final class WalkMessageDecoder extends MessageDecoder<WalkMessage> {

	@Override
	public WalkMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int length = packet.getLength();
		if (packet.getOpcode() == 213) {
			length -= 14; // strip off anti-cheat data
		}

		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];

		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		boolean run = reader.getUnsigned(DataType.BYTE) == 1;
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		for (int i = 0; i < steps; i++) {
			path[i][0] = (int) reader.getSigned(DataType.BYTE);
			path[i][1] = (int) reader.getSigned(DataType.BYTE, DataTransformation.SUBTRACT);
		}

		Position[] positions = new Position[steps + 1];
		positions[0] = new Position(x, y);
		for (int i = 0; i < steps; i++) {
			positions[i + 1] = new Position(path[i][0] + x, path[i][1] + y);
		}

		return new WalkMessage(positions, run);
	}

}