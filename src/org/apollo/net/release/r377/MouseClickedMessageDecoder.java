package org.apollo.net.release.r377;

import org.apollo.game.message.impl.MouseClickedMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link org.apollo.net.release.MessageDecoder} for the {@link org.apollo.game.message.impl.MouseClickedMessage}
 *
 * @author Stuart
 */
public final class MouseClickedMessageDecoder extends MessageDecoder<MouseClickedMessage> {

    @Override
    public MouseClickedMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int value = (int)reader.getUnsigned(DataType.INT);

        long delay = (value >> 20) * 50;

        boolean rightMouseButton = ((value >> 19) & 0x1) == 1;

        int cords = (value & 0x3FFFF);
        int x = cords % 765;
        int y = cords / 765;

        return new MouseClickedMessage(delay, rightMouseButton, x, y);
    }

}