package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to open an overlay interface.
 *
 * @author Major
 */
public final class OpenOverlayMessage extends Message {

	/**
	 * The overlay id.
	 */
	private final int overlayId;

	/**
	 * Creates the OpenSidebarMessage.
	 *
	 * @param overlayId The overlay id.
	 */
	public OpenOverlayMessage(int overlayId) {
		this.overlayId = overlayId;
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