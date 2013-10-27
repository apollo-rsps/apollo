package org.apollo.util.plugin;

/**
 * An {@link Exception} which is thrown when a dependency cannot be resolved,
 * or when there is a circular dependency.
 * @author Graham
 */
@SuppressWarnings("serial")
public final class DependencyException extends Exception {

	/**
	 * Creates the dependency exception.
	 * @param s The message describing what happened.
	 */
	public DependencyException(String s) {
		super(s);
	}

}
