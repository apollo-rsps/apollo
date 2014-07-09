package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FocusUpdateEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FocusUpdateEvent}.
 * 
 * @author Major
 */
public final class FocusUpdateEventDecoder extends EventDecoder<FocusUpdateEvent> {

    @Override
    public FocusUpdateEvent decode(GamePacket packet) {
	GamePacketReader decoder = new GamePacketReader(packet);
	return new FocusUpdateEvent(decoder.getUnsigned(DataType.BYTE) == 1);
    }

}