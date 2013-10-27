package org.apollo.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A utility class for wrapping old {@link Enumeration} objects inside an
 * {@link Iterator} to allow for greater compatibility.
 * @author Graham
 */
public final class EnumerationUtil {

	/**
	 * Returns an {@link Iterator} which wraps around the specified
	 * {@link Enumeration}.
	 * @param <E> The type of object that is iterated over.
	 * @param enumeration The {@link Enumeration}.
	 * @return An {@link Iterator}.
	 */
	public static <E> Iterator<E> asIterator(final Enumeration<E> enumeration) {
		return new Iterator<E>() {

			@Override
			public boolean hasNext() {
				return enumeration.hasMoreElements();
			}

			@Override
			public E next() {
				return enumeration.nextElement();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private EnumerationUtil() {

	}

}
