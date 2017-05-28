package org.apollo.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for {@link LanguageUtil}s.
 *
 * @author Graham
 */
public final class LanguageUtilTests {

	/**
	 * Tests the {@link LanguageUtil#getIndefiniteArticle} method.
	 */
	@Test
	public void indefiniteArticle() {
		assertEquals("an", LanguageUtil.getIndefiniteArticle("apple"));
		assertEquals("an", LanguageUtil.getIndefiniteArticle("urn"));
		assertEquals("a", LanguageUtil.getIndefiniteArticle("nose"));
		assertEquals("a", LanguageUtil.getIndefiniteArticle("foot"));

		assertEquals("an", LanguageUtil.getIndefiniteArticle("NPC"));
		assertEquals("an", LanguageUtil.getIndefiniteArticle("FC"));
	}

}