package org.apollo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * A test for the {@link TextUtil} class.
 *
 * @author Graham
 */
public class TestTextUtil {

	/**
	 * Tests the {@link TextUtil#capitalize} method.
	 */
	@Test
	public void testCapitalize() {
		String str = "tHiS is BAD capitAliZation. do You AGreE? YES!";
		String capitalized = "This is bad capitalization. Do you agree? Yes!";
		assertEquals(capitalized, TextUtil.capitalize(str));
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