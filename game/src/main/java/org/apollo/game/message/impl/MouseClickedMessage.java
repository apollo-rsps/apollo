package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to indicate when the mouse button/s have been clicked. This can be used in
 * combination with {@link FocusUpdateMessage} to work out if the player is clicking whilst the client is closed.
 *
 * @author Stuart
 * @author Major
 */
public final class MouseClickedMessage extends Message {

	/**
	 * The time, in milliseconds, since the last click.
	 */
	private final long clickDelay;

	/**
	 * Indicates whether or not the mouse button pressed was the right mouse button.
	 */
	private final boolean right;

	/**
	 * The y position of the cursor.
	 */
	private final int x;

	/**
	 * The x position of the cursor.
	 */
	private final int y;

	/**
	 * Creates the MouseClickedMessage.
	 *
	 * @param clickDelay The delay, in milliseconds, since the last click.
	 * @param right Whether or not the mouse button pressed was the right mouse button.
	 * @param x The x cursor position when clicked.
	 * @param y The y cursor position when clicked.
	 */
	public MouseClickedMessage(long clickDelay, boolean right, int x, int y) {
		this.clickDelay = clickDelay;
		this.right = right;
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the delay, in milliseconds, since the last click.
	 *
	 * @return The time delay.
	 */
	public long getClickDelay() {
		return clickDelay;
	}

	/**
	 * Gets the x position of the cursor.
	 *
	 * @return The x position of the cursor when clicked.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y position of the cursor.
	 *
	 * @return The y position of the cursor when clicked.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns whether or not the right mouse button was used.
	 *
	 * @return {@code true} if the right mouse button was used to click, {@code false} if not.
	 */
	public boolean wasRightMouseButton() {
		return right;
	}

}
