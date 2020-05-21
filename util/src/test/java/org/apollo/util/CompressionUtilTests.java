package org.apollo.util;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link CompressionUtil}s.
 *
 * @author Graham
 * @author Major
 */
public final class CompressionUtilTests {

	/**
	 * Tests the {@link CompressionUtil#bzip2} and {@link CompressionUtil#debzip2} methods.
	 */
	@Test
	public void bzip2() throws IOException {
		String string = "Hello, world!";

		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		CompressionUtil.debzip2(CompressionUtil.bzip2(data), data);

		assertEquals(string, new String(data, StandardCharsets.UTF_8));
	}

	/**
	 * Tests the {@link CompressionUtil#gzip} and {@link CompressionUtil#degzip} methods.
	 */
	@Test
	public void gzip() throws IOException {
		String string = "Hello, world!";

		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		CompressionUtil.degzip(CompressionUtil.gzip(data), data);

		assertEquals(string, new String(data, StandardCharsets.UTF_8));
	}

}