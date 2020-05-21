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
	 * Tests the {@link TextUtil#filterInvalidCharacters(String)} method.
	 */
	@Test
	public void testFilter() {
		String str = "this contains <<< invalid characters";
		String filtered = "this contains  invalid characters";
		assertEquals(filtered, TextUtil.filterInvalidCharacters(str));
	}

}