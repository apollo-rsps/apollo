package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Appearance;

/**
 * An event sent by the client when the player modifies their character's
 * design.
 * @author Graham
 */
public final class CharacterDesignEvent extends Event {

	/**
	 * The appearance.
	 */
	private final Appearance appearance;

	/**
	 * Creates the character design event.
	 * @param appearance The appearance.
	 */
	public CharacterDesignEvent(Appearance appearance) {
		this.appearance = appearance;
	}

	/**
	 * Gets the appearance.
	 * @return The appearance.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

}
