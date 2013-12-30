package org.apollo.net.release.r317;

import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link org.apollo.net.release.EventEncoder} for the {@link PrivacyOptionEvent}.
 *
 * @author Kyle Stevenson
 *         Date: 12/24/13
 *         Time: 1:44 AM
 */
public class PrivacyOptionEventEncoder extends EventEncoder<PrivacyOptionEvent> {
    @Override
    public GamePacket encode(final PrivacyOptionEvent event) {
        final GamePacketBuilder builder = new GamePacketBuilder(206, PacketType.FIXED);

        builder.put(DataType.BYTE, event.getPublicChat());
        builder.put(DataType.BYTE, event.getPrivateChat());
        builder.put(DataType.BYTE, event.getTradeCompete());

        return builder.toGamePacket();
    }
}
