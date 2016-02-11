package org.apollo.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link NameUtil}s.
 *
 * @author Major
 */
public final class NameUtilTests {

	/**
	 * Tests that attempting to encode an illegal character results in an {@link IllegalArgumentException} being
	 * thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void illegalCharacter() {
		NameUtil.encodeBase37("!");
	}

	/**
	 * Tests that attempting to decode an illegal encoded value results in an {@link IllegalArgumentException} being
	 * thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidName() {
		NameUtil.decodeBase37(NameUtil.encodeBase37("999999999999") + 1);
	}

	/**
	 * Tests that attempting to encode a String longer than 12 characters results in an
	 * {@link IllegalArgumentException} being thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void length() {
		NameUtil.encodeBase37("LongerThan12Characters");
	}

	/**
	 * Tests that encoding a String containing a space (' ') results in the space being replaced with an underscore
	 * ('_').
	 */
	@Test
	public void space() {
		assertEquals("hello_world", NameUtil.decodeBase37(NameUtil.encodeBase37("hello world")));
	}

	/**
	 * Tests that {@link NameUtil#decodeBase37} and {@link NameUtil#encodeBase37} perform as expected.
	 */
	@Test
	public void test() {
		assertEquals("a_b_c", NameUtil.decodeBase37(NameUtil.encodeBase37("a_b_c")));
		assertEquals("01234_56789", NameUtil.decodeBase37(NameUtil.encodeBase37("01234_56789")));
		assertEquals("a_b_c", NameUtil.decodeBase37(NameUtil.encodeBase37("A_B_C")));
	}

}
