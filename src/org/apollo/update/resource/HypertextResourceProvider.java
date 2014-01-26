package org.apollo.update.resource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * A {@link ResourceProvider} which provides additional hypertext resources.
 * 
 * @author Graham
 */
public final class HypertextResourceProvider extends ResourceProvider {

	/**
	 * The base directory from which documents are served.
	 */
	private final File base;

	/**
	 * Creates a new hypertext resource provider with the specified base directory.
	 * 
	 * @param base The base directory.
	 */
	public HypertextResourceProvider(File base) {
		this.base = base;
	}

	@Override
	public boolean accept(String path) throws IOException {
		File file = new File(base, path);
		URI target = file.toURI().normalize();
		if (target.toASCIIString().startsWith(base.toURI().normalize().toASCIIString())) {
			if (file.isDirectory()) {
				file = new File(file, "index.html");
			}
			return file.exists();
		}
		return false;
	}

	@Override
	public ByteBuffer get(String path) throws IOException {
		File file = new File(base, path);
		if (file.isDirectory()) {
			file = new File(file, "index.html");
		}
		if (!file.exists()) {
			return null;
		}

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		ByteBuffer buffer;
		try {
			buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
		} finally {
			raf.close();
		}

		return buffer;
	}

}