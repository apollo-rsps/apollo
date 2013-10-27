package org.apollo.util;

/**
 * A class which contains name-related utility methods.
 * @author Graham
 */
public final class NameUtil {

	/**
	 * An array of valid characters in a player name encoded as a long.
	 */
	private static final char[] NAME_CHARS = {
		'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
		'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
		'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@',
		'#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';',
		'.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`'
	};

	/**
	 * Converts a player name to a long.
	 * @param name The player name.
	 * @return The long.
	 */
	public static long encodeBase37(String name) {
		if (name.length() > 12) {
			throw new IllegalArgumentException("name too long");
		}
		long l = 0L;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		for (; l % 37L == 0L && l != 0L; l /= 37L);
		return l;
	}

	/**
	 * Converts a long to a player name.
	 * @param l The long.
	 * @return The player name.
	 */
	public static String decodeBase37(long l) {
		int i = 0;
		char[] chars = new char[12];
		while (l != 0L) {
			long tmp = l;
			l /= 37L;
			chars[11 - i++] = NAME_CHARS[(int) (tmp - l * 37L)];
		}
		return new String(chars, 12 - i, i);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private NameUtil() {

	}

}
