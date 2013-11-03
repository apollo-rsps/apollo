package org.apollo.util;

import java.nio.ByteBuffer;

import org.apollo.net.NetworkConstants;

/**
 * A utility class which contains {@link ByteBuffer}-related methods.
 * 
 * @author Graham
 */
public final class ByteBufferUtil {

	/**
	 * Reads a 'smart' (either a {@code byte} or {@code short} depending on the value) from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The 'smart'.
	 */
	public static int readSmart(ByteBuffer buffer) {
		int peek = buffer.get(buffer.position()) & 0xFF;
		if (peek < 128) {
			return buffer.get() & 0xFF;
		}
		return (buffer.getShort() & 0xFFFF) - 32768;
	}

	/**
	 * Reads a string from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String readString(ByteBuffer buffer) {
		StringBuilder bldr = new StringBuilder();
		char c;
		while ((c = (char) buffer.get()) != NetworkConstants.STRING_TERMINATOR) {
			bldr.append(c);
		}
		return bldr.toString();
	}

	/**
	 * Reads an unsigned tri byte from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The tri byte.
	 */
	public static int readUnsignedTriByte(ByteBuffer buffer) {
		return (buffer.get() & 0xFF) << 16 | (buffer.get() & 0xFF) << 8 | buffer.get() & 0xFF;
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private ByteBufferUtil() {

	}

}
