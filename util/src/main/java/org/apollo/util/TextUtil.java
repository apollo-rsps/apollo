package org.apollo.util;

/**
 * A class which contains text-related utility methods.
 *
 * @author Graham
 */
public final class TextUtil {

	/**
	 * An array of characters ordered by frequency - the elements with lower indices (generally) appear more often in
	 * chat messages.
	 */
	public static final char[] FREQUENCY_ORDERED_CHARS = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']'};

	/**
	 * Capitalizes the string correctly.
	 *
	 * @param string The input string.
	 * @return The string with correct capitalization.
	 */
	public static String capitalize(String string) {
		boolean capitalize = true;
		StringBuilder builder = new StringBuilder(string);
		int length = string.length();

		for (int index = 0; index < length; index++) {
			char character = builder.charAt(index);

			if (character == '.' || character == '!' || character == '?') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(character)) {
				builder.setCharAt(index, Character.toUpperCase(character));
				capitalize = false;
			} else {
				builder.setCharAt(index, Character.toLowerCase(character));
			}
		}

		return builder.toString();
	}

	/**
	 * Filters invalid characters from the specified string.
	 *
	 * @param str The input string.
	 * @return The filtered string.
	 */
	public static String filterInvalidCharacters(String str) {
		StringBuilder builder = new StringBuilder();
		for (char c : str.toLowerCase().toCharArray()) {
			for (char validChar : FREQUENCY_ORDERED_CHARS) {
				if (c == validChar) {
					builder.append(c);
					break;
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private TextUtil() {

	}

}