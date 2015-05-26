package org.apollo.util;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

/**
 * Contains tests for {@link ByteBuf} methods in {@link BufferUtil}.
 *
 * @author Graham
 */
public final class TestByteBufUtil {

	/**
	 * Test the {@link BufferUtil#readString(ByteBuf)} method.
	 */
	@Test
	public void testReadString() {
		ByteBuf buf = Unpooled.buffer(6);
		buf.writeBytes(new byte[] { 'H', 'e', 'l', 'l', 'o', 10 });
		String str = BufferUtil.readString(buf);
		assertEquals("Hello", str);

		buf = Unpooled.buffer(5);
		buf.writeBytes(new byte[] { 'W', 'o', 'r', 'l', 'd' });
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

}