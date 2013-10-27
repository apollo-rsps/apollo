package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to open an interface and temporary sidebar overlay.
 * @author Graham
 */
public final class OpenInterfaceSidebarEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The sidebar id.
	 */
	private final int sidebarId;

	/**
	 * Creates the open interface sidebar event.
	 * @param interfaceId The interface id.
	 * @param sidebarId The sidebar id.
	 */
	public OpenInterfaceSidebarEvent(int interfaceId, int sidebarId) {
		this.interfaceId = interfaceId;
		this.sidebarId = sidebarId;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the sidebar id.
	 * @return The sidebar id.
	 */
	public int getSidebarId() {
		return sidebarId;
	}

}
