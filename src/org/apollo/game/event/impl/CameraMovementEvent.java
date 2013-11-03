package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent when the user moves the camera.
 * 
 * @author Toby
 */
public class CameraMovementEvent extends Event {

	/**
	 * The camera x.
	 */
	private final int cameraX;

	/**
	 * The camera y.
	 */
	private final int cameraY;

	/**
	 * Creates a new camera movement event.
	 * 
	 * @param cameraX The camera x.
	 * @param cameraY The camera y.
	 */
	public CameraMovementEvent(int cameraX, int cameraY) {
		this.cameraX = cameraX;
		this.cameraY = cameraY;
	}

	/**
	 * Gets the camera x.
	 * 
	 * @return The camera x.
	 */
	public int getCameraX() {
		return cameraX;
	}

	/**
	 * Gets the camera y.
	 * 
	 * @return The camera y.
	 */
	public int getCameraY() {
		return cameraY;
	}

}