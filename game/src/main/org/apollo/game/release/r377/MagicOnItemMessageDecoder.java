package org.apollo.game.release.r377;

import org.apollo.game.message.impl.MagicOnItemMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link MagicOnItemMessage}.
 *
 * @author Chris Fletcher
 */
public final class MagicOnItemMessageDecoder extends MessageDecoder<MagicOnItemMessage> {

	@Override
	public MagicOnItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int spell = (int) reader.getUnsigned(DataType.SHORT);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new MagicOnItemMessage(interfaceId, id, slot, spell);
	}

}