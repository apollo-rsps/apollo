package org.apollo.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * A test for the {@link LanguageUtil} class.
 * @author Graham
 */
public class TestLanguageUtil {

	/**
	 * Tests the {@link LanguageUtil#getIndefiniteArticle(String)} method.
	 */
	@Test
	public void testIndefiniteArticle() {
		assertEquals("an", LanguageUtil.getIndefiniteArticle("apple"));
		assertEquals("an", LanguageUtil.getIndefiniteArticle("urn"));
		assertEquals("a",  LanguageUtil.getIndefiniteArticle("nose"));
		assertEquals("a",  LanguageUtil.getIndefiniteArticle("foot"));
	}

}
