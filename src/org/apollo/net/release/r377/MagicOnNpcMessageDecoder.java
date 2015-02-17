package org.apollo.net.release.r377;

import org.apollo.game.message.impl.MagicOnNpcMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link org.apollo.net.release.MessageDecoder} for the {@link org.apollo.game.message.impl.MagicOnNpcMessage}
 *
 * @author Stuart
 */
public final class MagicOnNpcMessageDecoder extends MessageDecoder<MagicOnNpcMessage> {

    @Override
    public MagicOnNpcMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        int spellId = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);

        return new MagicOnNpcMessage(index, spellId);
    }

}