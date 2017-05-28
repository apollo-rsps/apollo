package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to open a sidebar interface.
 *
 * @author Major
 */
public final class OpenSidebarMessage extends Message {

	/**
	 * The sidebar id.
	 */
	private final int sidebarId;

	/**
	 * Creates the OpenSidebarMessage.
	 *
	 * @param sidebarId The sidebar id.
	 */
	public OpenSidebarMessage(int sidebarId) {
		this.sidebarId = sidebarId;
	}

	/**
	 * Gets the sidebar id.
	 *
	 * @return The sidebar id.
	 */
	public int getSidebarId() {
		return sidebarId;
	}

}