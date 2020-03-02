package org.apollo.util;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
		// Reads a single byte from the buffer without modifying the current position.
		int peek = buffer.get(buffer.position()) & 0xFF;
		int value = peek > Byte.MAX_VALUE ? (buffer.getShort() & 0xFFFF) + Short.MIN_VALUE : buffer.get() & 0xFF;
		return value;
	}


	public static int readHugeSmart(ByteBuffer buffer) {
		int value = 0;
		int read;
		for (read = readSmart(buffer); read == 32767; read = readSmart(buffer)) {
			value += 32767;
		}
		value += read;
		return value;
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
		while ((character = (char) buffer.get()) != BufferUtil.STRING_TERMINATOR) {
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
		while (buffer.isReadable() && (character = buffer.readUnsignedByte()) != BufferUtil.STRING_TERMINATOR) {
			builder.append((char) character);
		}
		return builder.toString();
	}

	/**
	 * Reads a 24-bit medium integer from the specified {@link ByteBuffer}s current position and increases the buffers
	 * position by 3.
	 *
	 * @param buffer The {@link ByteBuffer} to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int readUnsignedMedium(ByteBuffer buffer) {
		return (buffer.getShort() & 0xFFFF) << 8 | buffer.get() & 0xFF;
	}

	/**
	 * Reads a 24-bit medium integer from the specified {@link ByteBuffer}s current position and increases the buffers
	 * position by 3.
	 *
	 * @param buffer The {@link ByteBuffer} to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int readUnsignedMedium(ByteBuf buffer) {
		return buffer.readUnsignedShort() << 8 | buffer.readUnsignedByte();
	}

	/**
	 * Reads a jagex-string from the specified {@link ByteBuf}.
	 *
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String readJagexString(ByteBuf buffer) {
		buffer.readByte();
		return readString(buffer);
	}


	/**
	 * Writes a string to the specified {@link ByteBuf}
	 * @param buffer
	 * @param str
	 */
	public static void writeString(ByteBuf buffer, String str) {
		byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
		buffer.writeBytes(bytes);
		buffer.writeByte(0);
	}

	/**
	 * Writes a string to the specified {@link ByteBuf}
	 * @param buffer
	 * @param str
	 */
	public static void writeString(ByteBuffer buffer, String str) {
		byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
		buffer.put(bytes);
		buffer.put((byte) 0);
	}

	/**
	 * The terminator of a string.
	 */
	public static final int STRING_TERMINATOR = 0;

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private BufferUtil() {

	}


}