package org.apollo.util;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * Contains tests for {@link BufferUtil}.
 *
 * @author Graham
 */
public class TestBufferUtil {

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

	/**
	 * Tests the {@link BufferUtil#readString(ByteBuffer)} method.
	 */
	@Test
	public void testReadString() {
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

}