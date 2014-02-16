package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to change the currently displayed tab interface.
 * 
 * @author Chris Fletcher
 */
public final class DisplayTabInterfaceEvent extends Event {

	/**
	 * The tab index.
	 */
	private final int tab;

	/**
	 * Creates a new display tab interface event.
	 * 
	 * @param tab The index of the tab to display.
	 */
	public DisplayTabInterfaceEvent(int tab) {
		this.tab = tab;
	}

	/**
	 * Gets the index of the tab to display.
	 * 
	 * @return The tab index.
	 */
	public int getTab() {
		return tab;
	}

}