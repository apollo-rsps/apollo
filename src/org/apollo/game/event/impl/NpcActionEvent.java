package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} representing the clicking of an npc menu action.
 * 
 * @author Major
 */
public class NpcActionEvent extends Event {

	/**
	 * The action number .
	 */
	private final int action;

	/**
	 * The npc index.
	 */
	private final int index;

	/**
	 * Creates a new npc action event.
	 * 
	 * @param action The action number.
	 * @param index The index of the npc.
	 */
	public NpcActionEvent(int action, int index) {
		this.action = action;
		this.index = index;
	}

	/**
	 * Gets the menu action number clicked.
	 * 
	 * @return The action number.
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Gets the index of the npc clicked.
	 * 
	 * @return The npc index.
	 */
	public int getNpcIndex() {
		return index;
	}

}