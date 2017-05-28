package org.apollo.game.release.r377;

import org.apollo.game.message.impl.MagicOnNpcMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link MagicOnNpcMessage}
 *
 * @author Stuart
 */
public final class MagicOnNpcMessageDecoder extends MessageDecoder<MagicOnNpcMessage> {

	@Override
	public MagicOnNpcMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int spell = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new MagicOnNpcMessage(index, spell);
	}

}