package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to change the interface of a tab.
 *
 * @author Graham
 */
public final class SwitchTabInterfaceMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The tab id.
	 */
	private final int tab;

	/**
	 * Creates the switch interface message.
	 *
	 * @param tab The tab id.
	 * @param interfaceId The interface id.
	 */
	public SwitchTabInterfaceMessage(int tab, int interfaceId) {
		this.tab = tab;
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the tab id.
	 *
	 * @return The tab id.
	 */
	public int getTabId() {
		return tab;
	}

}