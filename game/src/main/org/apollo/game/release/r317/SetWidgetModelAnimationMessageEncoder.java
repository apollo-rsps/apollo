package org.apollo.game.release.r317;

import org.apollo.game.message.impl.SetWidgetModelAnimationMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetWidgetModelAnimationMessage}.
 *
 * @author Chris Fletcher
 */
public final class SetWidgetModelAnimationMessageEncoder extends MessageEncoder<SetWidgetModelAnimationMessage> {

	@Override
	public GamePacket encode(SetWidgetModelAnimationMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(200);

		builder.put(DataType.SHORT, message.getInterfaceId() & 0xFFFF);
		builder.put(DataType.SHORT, message.getAnimation());

		return builder.toGamePacket();
	}

}