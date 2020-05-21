package org.apollo.game.release.r181.decoders.map;

import org.apollo.game.message.impl.decode.MapBuildCompleteMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class MapRebuildCompleteDecoder extends MessageDecoder<MapBuildCompleteMessage> {
	@Override
	public MapBuildCompleteMessage decode(GamePacket packet) {
		return new MapBuildCompleteMessage();
	}
}
