package org.apollo.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link TextUtil}s.
 *
 * @author Graham
 */
public class TextUtilTests {

	/**
	 * Tests the {@link TextUtil#capitalize} method.
	 */
	@Test
	public void testCapitalize() {
		String incorrect = "tHiS is BAD capitAliZation. do You AGreE? YES!";
		String correct = "This is bad capitalization. Do you agree? Yes!";
		assertEquals(correct, TextUtil.capitalize(incorrect));

		assertEquals("Test", TextUtil.capitalize("test"));
	}

	/**
	 * Tests the {@link TextUtil#compress} and {@link TextUtil#decompress} methods.
	 */
	@Test
	public void testCompression() {
		String str = "hello, world!";

		byte[] compressed = new byte[128];
		int len = TextUtil.compress(str, compressed);
		String uncompressed = TextUtil.decompress(compressed, len);

		assertEquals(str, uncompressed);
	}

	/**
	 * Tests the {@link TextUtil#filterInvalidCharacters(String)} method.
	 */
	@Test
	public void testFilter() {
		String str = "this contains <<< invalid characters";
		String filtered = "this contains  invalid characters";
		assertEquals(filtered, TextUtil.filterInvalidCharacters(str));
	}

}