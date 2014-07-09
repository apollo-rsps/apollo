package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client representing the clicking of an npc menu action. Note that the actual event sent
 * by the client is one of the three npc action events, but this is the event that should be intercepted (and the option
 * verified).
 * 
 * @author Major
 */
public abstract class NpcActionEvent extends Event {

    /**
     * The option number.
     */
    private final int option;

    /**
     * The index of the clicked npc.
     */
    private final int index;

    /**
     * Creates an npc action event.
     * 
     * @param option The option number.
     * @param index The index of the npc.
     */
    public NpcActionEvent(int option, int index) {
	this.option = option;
	this.index = index - 1;
    }

    /**
     * Gets the menu action number (i.e. the action event 'option') clicked.
     * 
     * @return The option number.
     */
    public int getOption() {
	return option;
    }

    /**
     * Gets the index of the npc clicked.
     * 
     * @return The npc index.
     */
    public int getIndex() {
	return index;
    }

}