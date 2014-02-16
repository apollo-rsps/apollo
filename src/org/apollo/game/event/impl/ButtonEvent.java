package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when a player clicks a button.
 * 
 * @author Graham
 */
public final class ButtonEvent extends Event {

	/**
	 * The widget id.
	 */
	private final int widgetId;

	/**
	 * Creates the button event.
	 * 
	 * @param widgetId The widget id.
	 */
	public ButtonEvent(int widgetId) {
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