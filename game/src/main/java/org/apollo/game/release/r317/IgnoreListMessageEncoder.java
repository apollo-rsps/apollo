package org.apollo.game.release.r317;

import java.util.List;

import org.apollo.game.message.impl.IgnoreListMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageEncoder} for the {@link IgnoreListMessage}.
 *
 * @author Major
 */
public final class IgnoreListMessageEncoder extends MessageEncoder<IgnoreListMessage> {

	@Override
	public GamePacket encode(IgnoreListMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(214, PacketType.VARIABLE_SHORT);

		List<String> usernames = message.getUsernames();
		for (String username : usernames) {
			builder.put(DataType.LONG, NameUtil.encodeBase37(username));
		}

		return builder.toGamePacket();
	}

}