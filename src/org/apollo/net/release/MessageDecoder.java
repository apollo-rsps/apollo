package org.apollo.net.release;

import org.apollo.game.message.Message;
import org.apollo.net.codec.game.GamePacket;

/**
 * A {@link MessageDecoder} decodes a {@link GamePacket} into a {@link Message} object which can be processed by the
 * server.
 * 
 * @author Graham
 * @param <M> The type of message.
 */
public abstract class MessageDecoder<M extends Message> {

	/**
	 * Decodes the specified packet into a message.
	 * 
	 * @param packet The packet.
	 * @return The message.
	 */
	public abstract M decode(GamePacket packet);

}