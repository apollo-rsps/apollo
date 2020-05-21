package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.RunClientScriptMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class RunClientScriptEncoder extends MessageEncoder<RunClientScriptMessage> {
	@Override
	public GamePacket encode(RunClientScriptMessage message) {
		final var builder = new GamePacketBuilder(62, PacketType.VARIABLE_SHORT);
		final var id = message.getId();
		final var params = message.getParams();

		final var signature = new StringBuilder();
		final var internal = new GamePacketBuilder();
		for (int i = params.length - 1; i >= 0; i--) {
			Object param = params[i];
			if (param instanceof String) {
				signature.append("s");
				internal.putString((String) param);
			} else {
				signature.append("i");
				internal.put(DataType.INT, (Number) param);
			}
		}

		builder.putString(signature.toString());
		builder.putBytes(internal);
		builder.put(DataType.INT, id);

		return builder.toGamePacket();
	}
}
