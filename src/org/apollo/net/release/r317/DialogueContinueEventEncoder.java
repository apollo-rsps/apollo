package org.apollo.net.release.r317;

import org.apollo.game.event.impl.DialogueContinueEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link DialogueContinueEvent}.
 * 
 * @author Chris Fletcher
 */
final class DialogueContinueEventDecoder extends EventDecoder<DialogueContinueEvent> {

    @Override
    public DialogueContinueEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
	return new DialogueContinueEvent(interfaceId);
    }

}