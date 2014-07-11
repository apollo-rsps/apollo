package org.apollo.net.release.r317;

import java.util.List;

import org.apollo.game.event.impl.IgnoreListEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventEncoder} for the {@link IgnoreListEvent}.
 * 
 * @author Major
 */
public final class IgnoreListEventEncoder extends EventEncoder<IgnoreListEvent> {

	@Override
	public GamePacket encode(IgnoreListEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(214, PacketType.VARIABLE_SHORT);

		List<String> usernames = event.getUsernames();
		for (String username : usernames) {
			builder.put(DataType.LONG, NameUtil.encodeBase37(username));
		}

		return builder.toGamePacket();
	}

}