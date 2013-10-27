package org.apollo.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A test for the {@link TextUtil} class.
 * @author Graham
 */
public class TestTextUtil {

	/**
	 * Tests the {@link TextUtil#compress(String, byte[])} and
	 * {@link TextUtil#uncompress(byte[], int)} methods.
	 */
	@Test
	public void testCompression() {
		String str = "hello, world!";

		byte[] compressed = new byte[128];
		int len = TextUtil.compress(str, compressed);
		String uncompressed = TextUtil.uncompress(compressed, len);

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

	/**
	 * Tets the {@link TextUtil#capitalize(String)} method.
	 */
	@Test
	public void testCapitalize() {
		String str = "tHiS is CRAP capitAliZation. do You AGreE? YES!";
		String capitalized = "This is crap capitalization. Do you agree? Yes!";
		assertEquals(capitalized, TextUtil.capitalize(str));
	}

}
