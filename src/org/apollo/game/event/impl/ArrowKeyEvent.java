package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when the user has pressed an arrow key.
 * 
 * @author Major
 */
public final class ArrowKeyEvent extends Event {

    /**
     * The camera roll.
     */
    private final int roll;

    /**
     * The camera yaw.
     */
    private final int yaw;

    /**
     * Creates a new arrow key event.
     */
    public ArrowKeyEvent(int roll, int yaw) {
	this.roll = roll;
	this.yaw = yaw;
    }

    /**
     * Gets the roll of the camera.
     * 
     * @return The roll.
     */
    public int getRoll() {
	return roll;
    }

    /**
     * Gets the yaw of the camera.
     * 
     * @return The yaw.
     */
    public int getYaw() {
	return yaw;
    }

}