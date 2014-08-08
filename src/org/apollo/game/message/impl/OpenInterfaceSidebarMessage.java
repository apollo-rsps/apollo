package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent to the client to open an interface and temporary sidebar overlay.
 * 
 * @author Graham
 */
public final class OpenInterfaceSidebarMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The sidebar id.
	 */
	private final int sidebarId;

	/**
	 * Creates the open interface sidebar message.
	 * 
	 * @param interfaceId The interface id.
	 * @param sidebarId The sidebar id.
	 */
	public OpenInterfaceSidebarMessage(int interfaceId, int sidebarId) {
		this.interfaceId = interfaceId;
		this.sidebarId = sidebarId;
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
	 * Gets the sidebar id.
	 * 
	 * @return The sidebar id.
	 */
	public int getSidebarId() {
		return sidebarId;
	}

}