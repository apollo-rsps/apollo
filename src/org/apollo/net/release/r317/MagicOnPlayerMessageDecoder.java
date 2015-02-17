package org.apollo.net.release.r317;

import org.apollo.game.message.impl.MagicOnPlayerMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link org.apollo.net.release.MessageDecoder} for the {@link org.apollo.game.message.impl.MagicOnPlayerMessage}
 *
 * @author Stuart
 */
public final class MagicOnPlayerMessageDecoder extends MessageDecoder<MagicOnPlayerMessage> {

    @Override
    public MagicOnPlayerMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

        int index = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
        int spellId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);

        return new MagicOnPlayerMessage(index, spellId);
    }

}