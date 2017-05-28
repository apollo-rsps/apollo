package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to
 *
 * @author Major
 */
public final class FlashTabInterfaceMessage extends Message {

	/**
	 * The id of the tab to flash.
	 */
	private final int tab;

	/**
	 * Creates the FlashTabInterfaceMessage.
	 *
	 * @param tab The id of the tab to flash.
	 */
	public FlashTabInterfaceMessage(int tab) {
		this.tab = tab;
	}

	/**
	 * Gets the id of the tab to flash.
	 *
	 * @return The id.
	 */
	public int getTab() {
		return tab;
	}

}