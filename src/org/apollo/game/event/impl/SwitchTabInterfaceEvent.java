package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to change the interface of a tab.
 * 
 * @author Graham
 */
public final class SwitchTabInterfaceEvent extends Event {

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * The tab id.
     */
    private final int tab;

    /**
     * Creates the switch interface event.
     * 
     * @param tab The tab id.
     * @param interfaceId The interface id.
     */
    public SwitchTabInterfaceEvent(int tab, int interfaceId) {
	this.tab = tab;
	this.interfaceId = interfaceId;
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
     * Gets the tab id.
     * 
     * @return The tab id.
     */
    public int getTabId() {
	return tab;
    }

}