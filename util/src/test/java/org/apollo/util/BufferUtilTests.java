package org.apollo.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link BufferUtil}s.
 *
 * @author Graham
 */
public final class BufferUtilTests {

	/**
	 * Test the {@link BufferUtil#readString(ByteBuf)} method.
	 */
	@Test
	public void byteBufString() {
		ByteBuf buffer = Unpooled.buffer(6 * Byte.BYTES);

		buffer.writeBytes(new byte[]{ 'H', 'e', 'l', 'l', 'o', 10 });
		assertEquals("Hello", BufferUtil.readString(buffer));

		buffer = Unpooled.buffer(2 * Byte.BYTES);
		buffer.writeByte('!');
		buffer.writeByte(BufferUtil.STRING_TERMINATOR);

		assertEquals("!", BufferUtil.readString(buffer));
	}

	/**
	 * Tests the {@link BufferUtil#readString(ByteBuffer)} method.
	 */
	@Test
	public void byteBufferString() {
		ByteBuffer buffer = ByteBuffer.allocate(4 * Byte.BYTES);
		buffer.put((byte) 'h');
		buffer.put((byte) 'i');
		buffer.put((byte) BufferUtil.STRING_TERMINATOR);
		buffer.put((byte) '!');
		buffer.flip();

		assertEquals("hi", BufferUtil.readString(buffer));
	}

	/**
	 * Tests the {@link BufferUtil#readUnsignedMedium} method.
	 */
	@Test
	public void medium() {
		ByteBuffer buf = ByteBuffer.allocate(3 * Byte.BYTES);
		buf.put((byte) 123);
		buf.put((byte) 45);
		buf.put((byte) 67);
		buf.flip();

		assertEquals(123 << 16 | 45 << 8 | 67, BufferUtil.readUnsignedMedium(buf));
	}

}