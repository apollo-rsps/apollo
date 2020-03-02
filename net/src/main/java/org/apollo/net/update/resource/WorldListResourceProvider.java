package org.apollo.net.update.resource;

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
		final var world = createWorld();
		final var buf = ByteBuffer.allocate(Integer.BYTES + world.remaining());

		buf.putInt(world.remaining());
		buf.put(world);

		return Optional.of(buf.flip());
	}

	private ByteBuffer createWorld() {
		final var address = "127.0.0.1";//10
		final var activity = "Converting Apollo";//18

		ByteBuffer world = ByteBuffer.allocate(Short.BYTES + Integer.BYTES + address.length() + activity
				.length() + Byte.BYTES + Byte.BYTES + Byte.BYTES + Short.BYTES);
		world.putShort((short) 1); // world id
		world.putInt(0); // world settings
		BufferUtil.writeString(world, address); // address
		BufferUtil.writeString(world, activity); // activity
		world.put((byte) 0); // country
		world.putShort((short) 2000);// number of players
		world.flip();

		ByteBuffer list = ByteBuffer.allocate(Short.BYTES + world.remaining());
		list.putShort((short) 1); // number of worlds
		list.put(world);

		return list.flip();
	}
}
