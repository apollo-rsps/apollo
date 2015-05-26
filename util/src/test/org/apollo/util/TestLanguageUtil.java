package org.apollo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for {@link LanguageUtil}.
 *
 * @author Graham
 */
public class TestLanguageUtil {

	/**
	 * Tests the {@link LanguageUtil#getIndefiniteArticle} method.
	 */
	@Test
	public void testIndefiniteArticle() {
		assertEquals("an", LanguageUtil.getIndefiniteArticle("apple"));
		assertEquals("an", LanguageUtil.getIndefiniteArticle("urn"));
		assertEquals("a", LanguageUtil.getIndefiniteArticle("nose"));
		assertEquals("a", LanguageUtil.getIndefiniteArticle("foot"));
	}

}
