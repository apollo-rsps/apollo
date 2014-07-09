package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to set a widget's displayed mob's animation.
 * 
 * @author Chris Fletcher
 */
public final class SetWidgetModelAnimationEvent extends Event {

    /**
     * The model's animation id.
     */
    private final int animation;

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * Creates a new set interface npc model's animation event.
     * 
     * @param interfaceId The interface id.
     * @param animation The model's animation id.
     */
    public SetWidgetModelAnimationEvent(int interfaceId, int animation) {
	this.interfaceId = interfaceId;
	this.animation = animation;
    }

    /**
     * Gets the model's mood id.
     * 
     * @return The model's mood id.
     */
    public int getAnimation() {
	return animation;
    }

    /**
     * Gets the interface id.
     * 
     * @return The interface id.
     */
    public int getInterfaceId() {
	return interfaceId;
    }

}