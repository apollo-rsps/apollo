package org.apollo.util;

import io.netty.buffer.ByteBuf;

import org.apollo.net.NetworkConstants;

/**
 * A utility class which provides extra {@link ByteBuf}-related methods which deal with data types used in the protocol.
 * 
 * @author Graham
 */
public final class ByteBufUtil {

	/**
	 * Reads a string from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String readString(ByteBuf buffer) {
		StringBuilder builder = new StringBuilder();
		int character;
		while (buffer.isReadable() && (character = buffer.readUnsignedByte()) != NetworkConstants.STRING_TERMINATOR) {
			builder.append((char) character);
		}
		return builder.toString();
	}

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private ByteBufUtil() {

	}

}