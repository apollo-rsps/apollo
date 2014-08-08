package org.apollo.game.message.impl;

/**
 * The first {@link NpcActionMessage}.
 * 
 * @author Major
 */
public final class FirstNpcActionMessage extends NpcActionMessage {

	/**
	 * Creates a new first npc action message.
	 * 
	 * @param index The index of the npc.
	 */
	public FirstNpcActionMessage(int index) {
		super(1, index);
	}

}