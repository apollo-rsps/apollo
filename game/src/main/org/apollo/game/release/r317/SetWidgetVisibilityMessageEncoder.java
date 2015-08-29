package org.apollo.game.release.r317;

import org.apollo.game.message.impl.SetWidgetVisibilityMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SetWidgetVisibilityMessage}.
 *
 * @author Major
 */
public final class SetWidgetVisibilityMessageEncoder extends MessageEncoder<SetWidgetVisibilityMessage> {

	@Override
	public GamePacket encode(SetWidgetVisibilityMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(171);

		builder.put(DataType.BYTE, message.isVisible() ? 0 : 1);
		builder.put(DataType.SHORT, message.getWidgetId());

		return builder.toGamePacket();
	}

}