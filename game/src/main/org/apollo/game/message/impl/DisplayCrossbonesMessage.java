package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to display crossbones when the player enters a multi-combat zone.
 *
 * @author Major
 */
public final class DisplayCrossbonesMessage extends Message {

	/**
	 * Whether or not the crossbones should be displayed.
	 */
	private final boolean display;

	/**
	 * Creates a display crossbones message.
	 *
	 * @param display Whether or not the crossbones should be displayed.
	 */
	public DisplayCrossbonesMessage(boolean display) {
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