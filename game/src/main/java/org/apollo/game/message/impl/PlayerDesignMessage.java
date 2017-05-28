package org.apollo.game.message.impl;

import org.apollo.game.model.Appearance;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when the player modifies their design.
 *
 * @author Graham
 */
public final class PlayerDesignMessage extends Message {

	/**
	 * The appearance.
	 */
	private final Appearance appearance;

	/**
	 * Creates the player design message.
	 *
	 * @param appearance The appearance.
	 */
	public PlayerDesignMessage(Appearance appearance) {
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