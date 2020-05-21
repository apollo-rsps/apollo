package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client representing the clicking of an npc menu action. Note that the actual message
 * sent by the client is one of the three npc action messages, but this is the message that should be intercepted (and
 * the option verified).
 *
 * @author Major
 */
public final class NpcActionMessage extends Message {

	/**
	 * The option number.
	 */
	private final int option;

	/**
	 * The index of the clicked npc.
	 */
	private final int index;

	/**
	 * Creates an npc action message.
	 *
	 * @param option The option number.
	 * @param index The index of the npc.
	 */
	public NpcActionMessage(int option, int index) {
		this.option = option;
		this.index = index;
	}

	/**
	 * Gets the menu action number (i.e. the action message 'option') clicked.
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