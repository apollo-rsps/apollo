package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FifthPlayerActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FifthPlayerActionEvent}.
 * 
 * @author Major
 */
public final class FifthPlayerActionEventDecoder extends EventDecoder<FifthPlayerActionEvent> {

    @Override
    public FifthPlayerActionEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
	return new FifthPlayerActionEvent(index);
    }

}