package org.apollo.game.model.event;

/**
 * The context of an {@link Event}.
 *
 * @author Major
 */
final class EventContext {

	/**
	 * Indicates whether or not the Event chain has been terminated.
	 */
	private boolean terminated;

	/**
	 * Terminates the Event chain.
	 */
	public void terminateEvent() {
		terminated = true;
	}

	/**
	 * Returns whether or not the Event chain has been terminated.
	 * 
	 * @return {@code true} if the Event chain has been terminated, otherwise {@code false}.
	 */
	public boolean terminated() {
		return terminated;
	}

}