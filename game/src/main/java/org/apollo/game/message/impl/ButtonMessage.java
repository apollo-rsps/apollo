package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player clicks a button.
 *
 * @author Graham
 */
public final class ButtonMessage extends Message {

	/**
	 * The widget id.
	 */
	private final int widgetId;

	/**
	 * Creates the button message.
	 *
	 * @param widgetId The widget id.
	 */
	public ButtonMessage(int widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * Gets the widget id.
	 *
	 * @return The widget id.
	 */
	public int getWidgetId() {
		return widgetId;
	}

}