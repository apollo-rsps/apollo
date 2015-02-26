package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent to the client to open an interface and temporary overlay.
 * 
 * @author Graham
 */
public final class OpenInterfaceOverlayMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The overlay id.
	 */
	private final int overlayId;

	/**
	 * Creates the open interface overlay message.
	 * 
	 * @param interfaceId The interface id.
	 * @param overlayId The overlay id.
	 */
	public OpenInterfaceOverlayMessage(int interfaceId, int overlayId) {
		this.interfaceId = interfaceId;
		this.overlayId = overlayId;
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
	 * Gets the overlay id.
	 * 
	 * @return The overlay id.
	 */
	public int getOverlayId() {
		return overlayId;
	}

}