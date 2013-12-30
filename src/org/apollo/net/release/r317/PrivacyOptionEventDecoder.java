package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link PrivacyOptionEvent}.
 *
 * @author Kyle Stevenson
 *         Date: 12/24/13
 *         Time: 1:44 AM
 */
public class PrivacyOptionEventDecoder extends EventDecoder<PrivacyOptionEvent> {
    @Override
    public PrivacyOptionEvent decode(final GamePacket packet) {
        final GamePacketReader reader = new GamePacketReader(packet);

        final int publicChat = (int) reader.getUnsigned(DataType.BYTE);
        final int privateChat = (int) reader.getUnsigned(DataType.BYTE);
        final int tradeCompete = (int) reader.getUnsigned(DataType.BYTE);

        return new PrivacyOptionEvent(publicChat, privateChat, tradeCompete);
    }
}
