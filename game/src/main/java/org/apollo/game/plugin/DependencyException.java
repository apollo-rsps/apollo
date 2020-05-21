package org.apollo.game.plugin;

/**
 * An {@link Exception} thrown when a dependency cannot be resolved, or when there is a circular dependency.
 *
 * @author Graham
 */
public final class DependencyException extends Exception {

	/**
	 * The generated serial version id.
	 */
	private static final long serialVersionUID = -3335727281501054641L;

	/**
	 * Creates the dependency exception.
	 *
	 * @param message The message describing what happened.
	 */
	public DependencyException(String message) {
		super(message);
	}

}