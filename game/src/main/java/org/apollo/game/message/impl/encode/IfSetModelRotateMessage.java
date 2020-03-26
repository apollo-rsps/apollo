package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type If set model rotate message.
 *
 * @author Khaled Abdeljaber
 */
public class IfSetModelRotateMessage extends Message {

	/**
	 * The packed interface.
	 */
	private final int packedInterface;

	/**
	 * The rotation in the x-axis.
	 */
	private final int rotationX;

	/**
	 * The rotation in the y-axis.
	 */
	private final int rotationY;

	/**
	 * Instantiates a new If set model rotate message.
	 *
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param rotationX   the rotation x
	 * @param rotationY   the rotation y
	 */
	public IfSetModelRotateMessage(int interfaceId, int componentId, int rotationX, int rotationY) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
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
	 * Gets rotation x.
	 *
	 * @return the rotation x
	 */
	public int getRotationX() {
		return rotationX;
	}

	/**
	 * Gets rotation y.
	 *
	 * @return the rotation y
	 */
	public int getRotationY() {
		return rotationY;
	}
}
