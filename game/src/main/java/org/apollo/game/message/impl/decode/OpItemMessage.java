package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

/**
 * The type Op item message.
 */
public class OpItemMessage extends Message {

	/**
	 * The option.
	 */
	private final int option;
	/**
	 * The item id.
	 */
	private final int id;
	/**
	 * The position of the item.
	 */
	private final int x, y;

	/**
	 * The movement type.
	 */
	private final int movementType;

	/**
	 * Instantiates a new option item message.
	 *
	 * @param id           the item id
	 * @param x            the x
	 * @param y            the y
	 * @param movementType the movement type
	 */
	public OpItemMessage(int option, int id, int x, int y, int movementType) {
		this.option = option;
		this.id = id;
		this.x = x;
		this.y = y;
		this.movementType = movementType;
	}


	/**
	 * Gets option.
	 *
	 * @return the option
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets item.
	 *
	 * @return the item
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Is movement type.
	 *
	 * @return the type of movement.
	 */
	public int getMovementType() {
		return movementType;
	}
}
