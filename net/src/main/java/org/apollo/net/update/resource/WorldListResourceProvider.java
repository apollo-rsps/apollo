package org.apollo.net.update.resource;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apollo.util.BufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

public class WorldListResourceProvider implements ResourceProvider {

	private static final String WORLDLIST_PATH = "/";

	@Override
	public boolean accept(String path) throws IOException {
		return path.equals(WORLDLIST_PATH);
	}

	@Override
	public Optional<ByteBuffer> get(String path) throws IOException {
		final var buf = Unpooled.buffer();
		final var world = createWorld();

		buf.writeInt(world.readableBytes());
		buf.writeBytes(world);

		return Optional.of(buf.nioBuffer());
	}

	private ByteBuf createWorld() {
		ByteBuf world = Unpooled.buffer();
		world.writeShort(1); // number of worlds

		world.writeShort(1); // world id
		world.writeInt(0); // world settings
		BufferUtil.writeString(world, "127.0.0.1"); // address
		BufferUtil.writeString(world, "Converting Apollo"); // address
		world.writeByte(0); // country
		world.writeShort(2000);// number of players
		return world;
	}
}
