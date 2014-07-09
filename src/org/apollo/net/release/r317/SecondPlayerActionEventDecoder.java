package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SecondPlayerActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link SecondPlayerActionEvent}.
 * 
 * @author Major
 */
public final class SecondPlayerActionEventDecoder extends EventDecoder<SecondPlayerActionEvent> {

    @Override
    public SecondPlayerActionEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
	return new SecondPlayerActionEvent(index);
    }

}