package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Appearance;

/**
 * An {@link Event} sent by the client when the player modifies their design.
 * 
 * @author Graham
 */
public final class PlayerDesignEvent extends Event {

	/**
	 * The appearance.
	 */
	private final Appearance appearance;

	/**
	 * Creates the player design event.
	 * 
	 * @param appearance The appearance.
	 */
	public PlayerDesignEvent(Appearance appearance) {
		this.appearance = appearance;
	}

	/**
	 * Gets the appearance.
	 * 
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

}