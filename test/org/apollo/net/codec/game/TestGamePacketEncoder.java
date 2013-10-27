package org.apollo.net.codec.game;

import static org.junit.Assert.*;

import net.burtleburtle.bob.rand.IsaacRandom;

import org.apollo.net.meta.PacketType;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

/**
 * A test for the {@link GamePacketEncoder} class.
 * @author Graham
 */
public class TestGamePacketEncoder {

	/**
	 * Tests the {@link GamePacketEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, Object)}
	 * method.
	 * @throws Exception if an error occurs.
	 */
	@Test
	public void testEncode() throws Exception {
		// generates 243, 141, 34, -223, 121...
		IsaacRandom random = new IsaacRandom(new int[] { 0, 0, 0, 0 });
		GamePacketEncoder encoder = new GamePacketEncoder(random);

		ChannelBuffer payload = ChannelBuffers.wrappedBuffer("Hello".getBytes());
		GamePacket packet = new GamePacket(10, PacketType.FIXED, payload.copy());
		ChannelBuffer buf = (ChannelBuffer) encoder.encode(null, null, packet);

		assertEquals(6, buf.readableBytes());
		assertEquals(253, buf.readUnsignedByte());
		assertEquals('H', buf.readUnsignedByte());
		assertEquals('e', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('o', buf.readUnsignedByte());

		packet = new GamePacket(9, PacketType.VARIABLE_BYTE, payload.copy());
		buf = (ChannelBuffer) encoder.encode(null, null, packet);

		assertEquals(7, buf.readableBytes());
		assertEquals(150, buf.readUnsignedByte());
		assertEquals(5, buf.readUnsignedByte());
		assertEquals('H', buf.readUnsignedByte());
		assertEquals('e', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('o', buf.readUnsignedByte());

		packet = new GamePacket(0, PacketType.VARIABLE_SHORT, payload.copy());
		buf = (ChannelBuffer) encoder.encode(null, null, packet);

		assertEquals(8, buf.readableBytes());
		assertEquals(34, buf.readUnsignedByte());
		assertEquals(5, buf.readUnsignedShort());
		assertEquals('H', buf.readUnsignedByte());
		assertEquals('e', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('o', buf.readUnsignedByte());
	}

}
