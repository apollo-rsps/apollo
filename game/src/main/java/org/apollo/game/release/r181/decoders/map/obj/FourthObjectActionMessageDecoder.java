package org.apollo.game.release.r181.decoders.map.obj;

import org.apollo.game.message.impl.decode.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the fourth {@link ObjectActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FourthObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new ObjectActionMessage(4, id, new Position(x, y), movementType);
	}

}
