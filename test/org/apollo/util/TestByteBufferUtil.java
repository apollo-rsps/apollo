package org.apollo.util;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.apollo.net.NetworkConstants;
import org.junit.Test;

/**
 * A test for the {@link ByteBufferUtil} class.
 * @author Graham
 */
public class TestByteBufferUtil {

	/**
	 * Tests the {@link ByteBufferUtil#readUnsignedTriByte(ByteBuffer)} method.
	 */
	@Test
	public void testReadUnsignedTriByte() {
		ByteBuffer buf = ByteBuffer.allocate(3);
		buf.put((byte) 123);
		buf.put((byte) 45);
		buf.put((byte) 67);
		buf.flip();

		assertEquals(8072515, ByteBufferUtil.readUnsignedTriByte(buf));
	}

	/**
	 * Tests the {@link ByteBufferUtil#readString(ByteBuffer)} method.
	 */
	@Test
	public void testReadString() {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put((byte) 'h');
		buf.put((byte) 'e');
		buf.put((byte) 'l');
		buf.put((byte) 'l');
		buf.put((byte) 'o');
		buf.put((byte) NetworkConstants.STRING_TERMINATOR);
		buf.put((byte) 66);
		buf.put((byte) 6);
		buf.flip();

		assertEquals("hello", ByteBufferUtil.readString(buf));
	}

}
