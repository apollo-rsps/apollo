package org.apollo.net.release.r317;

import org.apollo.game.message.impl.DialogueContinueMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link DialogueContinueMessage}.
 * 
 * @author Chris Fletcher
 */
final class DialogueContinueMessageDecoder extends MessageDecoder<DialogueContinueMessage> {

	@Override
	public DialogueContinueMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		return new DialogueContinueMessage(interfaceId);
	}

}