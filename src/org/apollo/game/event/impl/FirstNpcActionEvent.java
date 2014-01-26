package org.apollo.game.event.impl;

/**
 * The first {@link NpcActionEvent}.
 * 
 * @author Major
 */
public final class FirstNpcActionEvent extends NpcActionEvent {

	/**
	 * Creates a new first npc action event.
	 * 
	 * @param index The index of the npc.
	 */
	public FirstNpcActionEvent(int index) {
		super(1, index);
	}

}