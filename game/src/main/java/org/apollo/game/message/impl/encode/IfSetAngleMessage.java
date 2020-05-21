package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type If set angle message.
 *
 * @author Khaled Abdeljaber
 */
public class IfSetAngleMessage extends Message {

	/**
	 * The packed interface.
	 */
	private final int packedInterface;

	/**
	 * The zoom.
	 */
	private final int zoom;

	/**
	 * The rotation in the x axis.
	 */
	private final int rotationX;

	/**
	 * The rotation in the y axis.
	 */
	private final int rotationY;

	/**
	 * Instantiates a new If set angle message.
	 *
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param zoom        the zoom.
	 * @param rotationX   the rotationX
	 * @param rotationY   the rotationY
	 */
	public IfSetAngleMessage(int interfaceId, int componentId, int zoom, int rotationX, int rotationY) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.zoom = zoom;
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
	 * Gets zoom.
	 *
	 * @return the zoom
	 */
	public int getZoom() {
		return zoom;
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
