package org.apollo.game.event.impl;

/**
 * The second {@link NpcActionEvent}.
 * 
 * @author Major
 */
public class SecondNpcActionEvent extends NpcActionEvent {

	/**
	 * Creates a new second npc action event.
	 * 
	 * @param npcIndex The index of the npc.
	 */
	public SecondNpcActionEvent(int npcIndex) {
		super(2, npcIndex);
	}

}