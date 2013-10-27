package org.apollo.util;

import org.apollo.net.NetworkConstants;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A utility class which provides extra {@link ChannelBuffer}-related methods
 * which deal with data types used in the protocol.
 * @author Graham
 */
public final class ChannelBufferUtil {

	/**
	 * Reads a string from the specified buffer.
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String readString(ChannelBuffer buffer) {
		StringBuilder builder = new StringBuilder();
		int character;
		while (buffer.readable() && ((character = buffer.readUnsignedByte()) != NetworkConstants.STRING_TERMINATOR)) {
			builder.append((char) character);
		}
		return builder.toString();
	}

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private ChannelBufferUtil() {

	}

}
