package org.apollo.game.event.impl;

/**
 * The first {@link NpcActionEvent}.
 * 
 * @author Major
 */
public class FirstNpcActionEvent extends NpcActionEvent {

	/**
	 * Creates a new first npc action event.
	 * 
	 * @param npcIndex The index of the npc.
	 */
	public FirstNpcActionEvent(int npcIndex) {
		super(1, npcIndex);
	}

}