package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client indicating a flashing tab has been clicked.
 *
 * @author Major
 */
public final class FlashingTabClickedMessage extends Message {

	/**
	 * The tab that was clicked.
	 */
	private final int tab;

	/**
	 * Creates the FlashingTabClickedMessage.
	 *
	 * @param tab The tab that was clicked.
	 */
	public FlashingTabClickedMessage(int tab) {
		this.tab = tab;
	}

	/**
	 * Gets the index of the tab that was clicked.
	 *
	 * @return The tab index.
	 */
	public int getTab() {
		return tab;
	}

}