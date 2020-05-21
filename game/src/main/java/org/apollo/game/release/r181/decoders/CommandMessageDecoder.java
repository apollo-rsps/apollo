package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.CommandMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link CommandMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class CommandMessageDecoder extends MessageDecoder<CommandMessage> {

	@Override
	public CommandMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		return new CommandMessage(reader.getString());
	}

}