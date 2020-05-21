package org.apollo.game.release.r181.decoders.map;

import org.apollo.game.message.impl.decode.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link WalkMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class WalkMessageDecoder extends MessageDecoder<WalkMessage> {

	@Override
	public WalkMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int y = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		long type = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);

		return new WalkMessage(x, y, type == 1);
	}

}