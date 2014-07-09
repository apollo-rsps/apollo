package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FourthPlayerActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FourthPlayerActionEvent}.
 * 
 * @author Major
 */
public final class FourthPlayerActionEventDecoder extends EventDecoder<FourthPlayerActionEvent> {

    @Override
    public FourthPlayerActionEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
	return new FourthPlayerActionEvent(index);
    }

}