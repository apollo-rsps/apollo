package org.apollo.util;

import static org.junit.Assert.*;

import org.apollo.util.ChannelBufferUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

/**
 * A test for the {@link ChannelBufferUtil} class.
 * @author Graham
 */
public final class TestChannelBufferUtil {

	/**
	 * Test the {@link ChannelBufferUtil#readString(ChannelBuffer)}
	 * method.
	 */
	@Test
	public void testReadString() {
		ChannelBuffer buf = ChannelBuffers.buffer(6);
		buf.writeBytes(new byte[] { 'H', 'e', 'l', 'l', 'o', 10 });
		String str = ChannelBufferUtil.readString(buf);
		assertEquals("Hello", str);

		buf = ChannelBuffers.buffer(5);
		buf.writeBytes(new byte[] { 'W', 'o', 'r', 'l', 'd' });
		str = ChannelBufferUtil.readString(buf);
		assertEquals("World", str);

		buf = ChannelBuffers.buffer(3);
		buf.writeByte('!');
		buf.writeByte(10);
		buf.writeByte('.');

		str = ChannelBufferUtil.readString(buf);
		assertEquals("!", str);

		str = ChannelBufferUtil.readString(buf);
		assertEquals(".", str);
	}

}
