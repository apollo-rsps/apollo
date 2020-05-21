package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client representing the clicking of a player menu action. Note that the actual message
 * sent by the client is one of the five player action messages, but this is the message that should be intercepted
 * (and
 * the option verified).
 *
 * @author Major
 */
public final class PlayerActionMessage extends Message {

	/**
	 * The option number.
	 */
	private final int option;

	/**
	 * The index of the clicked player.
	 */
	private final int index;

	/**
	 * The movement type that the player performs.
	 */
	private final int movementType;

	/**
	 * Creates a player action message.
	 *  @param option The option number.
	 * @param index The index of the player.
	 * @param movementType The type of movement.
	 */
	public PlayerActionMessage(int option, int index, int movementType) {
		this.option = option;
		this.index = index;
		this.movementType = movementType;
	}

	/**
	 * Gets the index of the clicked player.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets movement type.
	 *
	 * @return the movement type
	 */
	public int getMovementType() {
		return movementType;
	}

	/**
	 * Gets the menu action number (i.e. the action message 'option') clicked.
	 *
	 * @return The option number.
	 */
	public int getOption() {
		return option;
	}

}