package org.apollo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class which contains {@link InputStream}- and {@link OutputStream}-related utility methods.
 * 
 * @author Graham
 */
public final class StreamUtil {

	/**
	 * Reads a string from the specified input stream.
	 * 
	 * @param is The input stream.
	 * @return The string.
	 * @throws IOException If an I/O error occurs.
	 */
	public static String readString(InputStream is) throws IOException {
		StringBuilder builder = new StringBuilder();
		char character;
		while ((character = (char) is.read()) != -1 && character != '\0') {
			builder.append(character);
		}
		return builder.toString();
	}

	/**
	 * Writes a string to the specified output stream.
	 * 
	 * @param os The output stream.
	 * @param str The string.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void writeString(OutputStream os, String str) throws IOException {
		for (char c : str.toCharArray()) {
			os.write(c);
		}
		os.write('\0');
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private StreamUtil() {

	}

}