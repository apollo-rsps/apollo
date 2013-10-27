package org.apollo.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

/**
 * A test for the {@link CompressionUtil} class.
 * @author Graham
 */
public class TestCompressionUtil {

	/**
	 * Tests the {@link CompressionUtil#gzip(byte[])} and
	 * {@link CompressionUtil#ungzip(byte[], byte[])} methods.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testGzip() throws IOException {
		String str = "Hello, World!";
		byte[] data = str.getBytes();
		byte[] compressed = CompressionUtil.gzip(data);
		CompressionUtil.ungzip(compressed, data);
		assertEquals(str, new String(data));
	}

	/**
	 * Tests the {@link CompressionUtil#bzip2(byte[])} and
	 * {@link CompressionUtil#unbzip2(byte[], byte[])} methods.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testBzip2() throws IOException {
		String str = "Hello, World!";
		byte[] data = str.getBytes();
		byte[] compressed = CompressionUtil.bzip2(data);
		CompressionUtil.unbzip2(compressed, data);
		assertEquals(str, new String(data));
	}

}
