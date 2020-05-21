package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when the player clicks with their mouse (or mousekeys etc).
 *
 * @author Major
 */
public final class FlaggedMouseEventMessage extends Message {

	/**
	 * The number of clicks on this point (i.e. the point ({@link #x}, {@link #y})).
	 */
	private final int clickCount;

	/**
	 * The x coordinate of the mouse click.
	 */
	private final int x;

	/**
	 * The y coordinate of the mouse click.
	 */
	private final int y;

	/**
	 * Indicates whether the {@link #x} and {@link #y} values represent the deviation from the last click or an actual
	 * point.
	 */
	private final boolean delta;

	/**
	 * Creates a new mouse click message.
	 *
	 * @param clickCount The number of clicks on this point.
	 * @param x The x coordinate of the mouse click.
	 * @param y The y coordinate of the mouse click.
	 * @param delta If the coordinates represent a change in x/y, rather than the values themselves.
	 */
	public FlaggedMouseEventMessage(int clickCount, int x, int y, boolean delta) {
		this.clickCount = clickCount;
		this.x = x;
		this.y = y;
		this.delta = delta;
	}

	/**
	 * Gets the number of clicks on this point - maximum value of 2047.
	 *
	 * @return The number of clicks.
	 */
	public int getClickCount() {
		return clickCount;
	}

	/**
	 * The x coordinate of the click.
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * The y coordinate of the click.
	 *
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the value indicating whether the {@link #x} and {@link #y} values represent the deviation from the last
	 * click or an actual point.
	 *
	 * @return The value.
	 */
	public boolean getDelta() {
		return delta;
	}

}