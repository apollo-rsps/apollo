package org.apollo.game.release.r181.decoders.map.obj;

import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the sixth {@link ObjectActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class SixthObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new ObjectActionMessage(6, id, new Position(-1, -1), 0);
	}

}
