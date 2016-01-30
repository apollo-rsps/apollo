package org.apollo.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link CompressionUtil}s.
 *
 * @author Graham
 */
public class CompressionUtilTests {

	/**
	 * Tests the {@link CompressionUtil#bzip2(byte[])} and {@link CompressionUtil#debzip2} methods.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
	@Test
	public void testBzip2() throws IOException {
		String str = "Hello, World!";
		byte[] data = str.getBytes();
		byte[] compressed = CompressionUtil.bzip2(data);
		CompressionUtil.debzip2(compressed, data);
		assertEquals(str, new String(data));
	}

	/**
	 * Tests the {@link CompressionUtil#gzip(byte[])} and {@link CompressionUtil#degzip} methods.
	 *
	 * @throws IOException If an I/O error occurs.
	 */
	@Test
	public void testGzip() throws IOException {
		String str = "Hello, World!";
		byte[] data = str.getBytes();
		byte[] compressed = CompressionUtil.gzip(data);
		CompressionUtil.degzip(compressed, data);
		assertEquals(str, new String(data));
	}

}