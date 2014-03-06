package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to display crossbones when the player enters a multi-combat zone.
 * 
 * @author Major
 */
public final class DisplayCrossbonesEvent extends Event {

	/**
	 * Whether or not the crossbones should be displayed.
	 */
	private final boolean display;

	/**
	 * Creates a display crossbones event.
	 * 
	 * @param display Whether or not the crossbones should be displayed.
	 */
	public DisplayCrossbonesEvent(boolean display) {
		this.display = display;
	}

	/**
	 * Indicates whether the crossbones will be displayed.
	 * 
	 * @return {@code true} if the crossbones will be displayed, otherwise {@code false}.
	 */
	public boolean isDisplayed() {
		return display;
	}

}