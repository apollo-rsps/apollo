package org.apollo.util;

/**
 * A utility class which contains language-related methods.
 * @author Graham
 */
public final class LanguageUtil {

	/**
	 * Gets the indefinite article of a 'thing'.
	 * @param thing The thing.
	 * @return The indefinite article.
	 */
	public static String getIndefiniteArticle(String thing) {
		char first = thing.toLowerCase().charAt(0);
		boolean vowel = first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u';
		return vowel ? "an" : "a";
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private LanguageUtil() {

	}

}
