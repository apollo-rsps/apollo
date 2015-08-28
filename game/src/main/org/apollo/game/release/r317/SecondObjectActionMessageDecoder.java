package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the second {@link ObjectActionMessage}.
 *
 * @author Graham
 */
public final class SecondObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new ObjectActionMessage(2, id, new Position(x, y));
	}

}