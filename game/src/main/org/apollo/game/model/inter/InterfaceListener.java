package org.apollo.game.model.inter;

/**
 * Listens to interface-related messages.
 *
 * @author Graham
 */
@FunctionalInterface
public interface InterfaceListener {

	/**
	 * Called when the interface has been closed.
	 */
	public void interfaceClosed();

}