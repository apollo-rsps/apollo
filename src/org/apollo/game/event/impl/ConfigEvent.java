package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to adjust a certain config or attribute setting.
 * 
 * @author Chris Fletcher
 */
public final class ConfigEvent extends Event {

    /**
     * The identifier.
     */
    private final int id;

    /**
     * The value.
     */
    private final int value;

    /**
     * Creates a new config event.
     * 
     * @param id The config's identifier.
     * @param value The value.
     */
    public ConfigEvent(int id, int value) {
	this.id = id;
	this.value = value;
    }

    /**
     * Gets the config's identifier.
     * 
     * @return The config id.
     */
    public int getId() {
	return id;
    }

    /**
     * Gets the config's value.
     * 
     * @return The config value.
     */
    public int getValue() {
	return value;
    }

}