package org.apollo.game.release.r181.decoders.map.obj;

import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the fifth {@link ObjectActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FifthObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		return new ObjectActionMessage(5, id, new Position(x, y), movementType);
	}

}
