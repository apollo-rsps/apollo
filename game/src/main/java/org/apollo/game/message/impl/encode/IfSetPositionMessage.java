package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type If set position message.
 *
 * @author Khaled Abdeljaber
 */
public class IfSetPositionMessage extends Message {

	private final int packedInterface;
	private final int x, y;

	/**
	 * Instantiates a new If set position message.
	 *
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param x           the x
	 * @param y           the y
	 */
	public IfSetPositionMessage(int interfaceId, int componentId, int x, int y) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets packed interface.
	 *
	 * @return the packed interface
	 */
	public int getPackedInterface() {
		return packedInterface;
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
}
