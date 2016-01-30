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
	public void readByteBufString() {
		ByteBuf buf = Unpooled.buffer(6);
		buf.writeBytes(new byte[]{ 'H', 'e', 'l', 'l', 'o', 10 });
		String str = BufferUtil.readString(buf);
		assertEquals("Hello", str);

		buf = Unpooled.buffer(5);
		buf.writeBytes(new byte[]{ 'W', 'o', 'r', 'l', 'd' });
		str = BufferUtil.readString(buf);
		assertEquals("World", str);

		buf = Unpooled.buffer(3);
		buf.writeByte('!');
		buf.writeByte(10);
		buf.writeByte('.');

		str = BufferUtil.readString(buf);
		assertEquals("!", str);

		str = BufferUtil.readString(buf);
		assertEquals(".", str);
	}

	/**
	 * Tests the {@link BufferUtil#readString(ByteBuffer)} method.
	 */
	@Test
	public void readByteBufferString() {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put((byte) 'h');
		buf.put((byte) 'e');
		buf.put((byte) 'l');
		buf.put((byte) 'l');
		buf.put((byte) 'o');
		buf.put((byte) BufferUtil.STRING_TERMINATOR);
		buf.put((byte) 66);
		buf.put((byte) 6);
		buf.flip();

		assertEquals("hello", BufferUtil.readString(buf));
	}

	/**
	 * Tests the {@link BufferUtil#readUnsignedMedium} method.
	 */
	@Test
	public void testReadUnsignedTriByte() {
		ByteBuffer buf = ByteBuffer.allocate(3);
		buf.put((byte) 123);
		buf.put((byte) 45);
		buf.put((byte) 67);
		buf.flip();

		assertEquals(8072515, BufferUtil.readUnsignedMedium(buf));
	}

}