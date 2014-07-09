package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client that changes the state of a hidden widget component (e.g. the special attack bar
 * on the weapon tab).
 * 
 * @author Chris Fletcher
 */
public final class SetWidgetVisibilityEvent extends Event {

    /**
     * The component id.
     */
    private final int component;

    /**
     * Visible flag.
     */
    private final boolean visible;

    /**
     * Creates the interface component state event.
     * 
     * @param component The compononent id.
     * @param visible The flag for showing or hiding the component.
     */
    public SetWidgetVisibilityEvent(int component, boolean visible) {
	this.component = component;
	this.visible = visible;
    }

    /**
     * Gets the id of the interface component.
     * 
     * @return The component id.
     */
    public int getWidgetId() {
	return component;
    }

    /**
     * Gets the visible flag.
     * 
     * @return {@code true} if the component has been set to visible, {@code false} if not.
     */
    public boolean isVisible() {
	return visible;
    }

}