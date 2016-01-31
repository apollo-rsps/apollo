package org.apollo.util;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link StreamUtil}s.
 *
 * @author Major
 */
public final class StreamUtilTests {

	/**
	 * Tests that reading a String from an {@link InputStream} works as expected.
	 */
	@Test
	public void read() throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream("hello\0".getBytes(StandardCharsets.US_ASCII));
		assertEquals("hello", StreamUtil.readString(input));
	}

	/**
	 * Tests that attempting to read a String that is <strong>not</strong> terminated with {@code \0} will not cause
	 * an infinite loop.
	 */
	@Test(timeout = 10)
	public void unterminated() throws IOException {
		InputStream input = new ByteArrayInputStream("hello".getBytes(StandardCharsets.US_ASCII));
		StreamUtil.readString(input);
	}

	/**
	 * Tests that writing a String to an {@link OutputStream} works as expected.
	 */
	@Test
	public void write() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		StreamUtil.writeString(output, "hello");

		String written = new String(output.toByteArray(), StandardCharsets.US_ASCII);
		assertEquals("hello\0", written);
	}

}
