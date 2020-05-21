package org.apollo.game.release.r317;

import org.apollo.game.message.impl.OpenDialogueOverlayMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link OpenDialogueOverlayMessage}.
 *
 * @author Major
 */
public final class OpenDialogueOverlayMessageEncoder extends MessageEncoder<OpenDialogueOverlayMessage> {

	@Override
	public GamePacket encode(OpenDialogueOverlayMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(218);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getInterfaceId());
		return builder.toGamePacket();
	}

}