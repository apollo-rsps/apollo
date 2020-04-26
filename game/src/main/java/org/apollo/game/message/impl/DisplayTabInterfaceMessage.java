package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to change the currently displayed tab interface.
 *
 * @author Chris Fletcher
 */
public final class DisplayTabInterfaceMessage extends Message {

	/**
	 * The tab index.
	 */
	private final int tab;

	/**
	 * Creates a new display tab interface message.
	 *
	 * @param tab The index of the tab to display.
	 */
	public DisplayTabInterfaceMessage(int tab) {
		this.tab = tab;
	}

	/**
	 * Gets the index of the tab to display.
	 *
	 * @return The tab index.
	 */
	public int getTab() {
		return tab;
	}

}