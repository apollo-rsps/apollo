package org.apollo.net.codec.game;

/**
 * An enumeration which holds the mode a {@link GamePacketBuilder} or
 * {@link GamePacketReader} can be in.
 * @author Graham
 */
public enum AccessMode {

	/**
	 * When in byte access modes, bytes are written directly to the buffer.
	 */
	BYTE_ACCESS,

	/**
	 * When in bit access mode, bits can be written and packed into bytes.
	 */
	BIT_ACCESS;

}
