package org.apollo.util;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

import org.apollo.net.NetworkConstants;

/**
 * A utility class which contains {@link ByteBuffer}-related utility methods.
 * 
 * @author Graham
 */
public final class BufferUtil {

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
     * Reads a string from the specified {@link ByteBuffer}.
     * 
     * @param buffer The buffer.
     * @return The string.
     */
    public static String readString(ByteBuffer buffer) {
	StringBuilder bldr = new StringBuilder();
	char character;
	while ((character = (char) buffer.get()) != NetworkConstants.STRING_TERMINATOR) {
	    bldr.append(character);
	}
	return bldr.toString();
    }

    /**
     * Reads a string from the specified {@link ByteBuf}.
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
     * Reads an unsigned tri-byte from the specified {@link ByteBuffer}.
     * 
     * @param buffer The buffer.
     * @return The tri-byte.
     */
    public static int readUnsignedTriByte(ByteBuffer buffer) {
	return (buffer.get() & 0xFF) << 16 | (buffer.get() & 0xFF) << 8 | buffer.get() & 0xFF;
    }

    /**
     * Default private constructor to prevent instantiation.
     */
    private BufferUtil() {

    }

}