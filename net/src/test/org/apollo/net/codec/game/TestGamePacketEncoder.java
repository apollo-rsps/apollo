package org.apollo.net.codec.game;

import static org.junit.Assert.assertEquals;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import org.apollo.net.meta.PacketType;
import org.apollo.util.security.IsaacRandom;
import org.junit.Test;

/**
 * Contains tests for {@link GamePacketEncoder}.
 *
 * @author Graham
 */
public class TestGamePacketEncoder {

	/**
	 * Tests the {@link GamePacketEncoder#encode} method.
	 *
	 * @throws Exception If an error occurs.
	 */
	@Test
	public void testEncode() throws Exception {
		// generates 243, 141, 34, -223, 121...
		IsaacRandom random = new IsaacRandom(new int[] { 0, 0, 0, 0 });
		GamePacketEncoder encoder = new GamePacketEncoder(random);

		ByteBuf payload = Unpooled.wrappedBuffer("Hello".getBytes());
		GamePacket packet = new GamePacket(10, PacketType.FIXED, payload.copy());

		List<Object> out = new ArrayList<>();
		encoder.encode(null, packet, out);

		ByteBuf buf = (ByteBuf) out.get(0);
		assertEquals(6, buf.readableBytes());
		assertEquals(253, buf.readUnsignedByte());
		assertEquals('H', buf.readUnsignedByte());
		assertEquals('e', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('o', buf.readUnsignedByte());

		packet = new GamePacket(9, PacketType.VARIABLE_BYTE, payload.copy());
		out.clear();
		encoder.encode(null, packet, out);
		buf = (ByteBuf) out.get(0);

		assertEquals(7, buf.readableBytes());
		assertEquals(150, buf.readUnsignedByte());
		assertEquals(5, buf.readUnsignedByte());
		assertEquals('H', buf.readUnsignedByte());
		assertEquals('e', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('l', buf.readUnsignedByte());
		assertEquals('o', buf.readUnsignedByte());

		packet = new GamePacket(0, PacketType.VARIABLE_SHORT, payload.copy());
		out.clear();
		encoder.encode(null, packet, out);
		buf = (ByteBuf) out.get(0);

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