package org.apollo.net.codec.game;

import org.apollo.net.meta.PacketType;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Represents a single packet used in the in-game protocol.
 * @author Graham
 */
public final class GamePacket {

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The packet type.
	 */
	private final PacketType type;

	/**
	 * The length.
	 */
	private final int length;

	/**
	 * The payload.
	 */
	private final ChannelBuffer payload;

	/**
	 * Creates the game packet.
	 * @param opcode The opcode.
	 * @param type The packet type.
	 * @param payload The payload.
	 */
	public GamePacket(int opcode, PacketType type, ChannelBuffer payload) {
		this.opcode = opcode;
		this.type = type;
		this.length = payload.readableBytes();
		this.payload = payload;
	}

	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the payload length.
	 * @return The payload length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the payload.
	 * @return The payload.
	 */
	public ChannelBuffer getPayload() {
		return payload;
	}

	/**
	 * Gets the packet type.
	 * @return The packet type.
	 */
	public PacketType getType() {
		return type;
	}

}
