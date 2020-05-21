package org.apollo.game.release.r181.decoders.map.obj;

import org.apollo.game.message.impl.decode.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the second {@link ObjectActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class SecondObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT);
		int y = (int) reader.getUnsigned(DataType.SHORT);
		int x = (int) reader.getUnsigned(DataType.SHORT);

		return new ObjectActionMessage(2, id, new Position(x, y), movementType);
	}

}