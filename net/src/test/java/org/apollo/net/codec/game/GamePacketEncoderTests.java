package org.apollo.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apollo.net.meta.PacketType;
import org.apollo.util.security.IsaacRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for {@link GamePacketEncoder}.
 *
 * @author Graham
 */
public class GamePacketEncoderTests {

	/**
	 * Tests the {@link GamePacketEncoder#encode} method.
	 *
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void encode() throws Exception {
		// generates 243, 141, 34, -223, 121...
		IsaacRandom random = new IsaacRandom(new int[] { 0, 0, 0, 0 });
		GamePacketEncoder encoder = new GamePacketEncoder(random);

		ByteBuf payload = Unpooled.wrappedBuffer("Hello".getBytes());
		GamePacket packet = new GamePacket(10, PacketType.FIXED, payload.copy());

		ByteBuf out = Unpooled.buffer();
		encoder.encode(null, packet, out);

		assertEquals(6, out.readableBytes());
		assertEquals(253, out.readUnsignedByte());
		assertEquals('H', out.readUnsignedByte());
		assertEquals('e', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('o', out.readUnsignedByte());

		packet = new GamePacket(9, PacketType.VARIABLE_BYTE, payload.copy());
		out.clear();
		encoder.encode(null, packet, out);

		assertEquals(7, out.readableBytes());
		assertEquals(150, out.readUnsignedByte());
		assertEquals(5, out.readUnsignedByte());
		assertEquals('H', out.readUnsignedByte());
		assertEquals('e', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('o', out.readUnsignedByte());

		packet = new GamePacket(0, PacketType.VARIABLE_SHORT, payload.copy());
		out.clear();
		encoder.encode(null, packet, out);

		assertEquals(8, out.readableBytes());
		assertEquals(34, out.readUnsignedByte());
		assertEquals(5, out.readUnsignedShort());
		assertEquals('H', out.readUnsignedByte());
		assertEquals('e', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('l', out.readUnsignedByte());
		assertEquals('o', out.readUnsignedByte());
	}

}