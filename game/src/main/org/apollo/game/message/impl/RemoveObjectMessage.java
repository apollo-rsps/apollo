package org.apollo.game.message.impl;

import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to remove an object from a tile.
 *
 * @author Major
 */
public final class RemoveObjectMessage extends RegionUpdateMessage {

	/**
	 * The orientation of the object.
	 */
	private final int orientation;

	/**
	 * The position of the object.
	 */
	private final int positionOffset;

	/**
	 * The type of the object.
	 */
	private final int type;

	/**
	 * Creates the RemoveObjectMessage.
	 *
	 * @param object The {@link GameObject} to send.
	 * @param positionOffset The offset of the GameObject's Position from the Region's top-left position.
	 */
	public RemoveObjectMessage(GameObject object, int positionOffset) {
		this.positionOffset = positionOffset;
		type = object.getType();
		orientation = object.getOrientation();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoveObjectMessage) {
			RemoveObjectMessage other = (RemoveObjectMessage) obj;
			return type == other.type && orientation == other.orientation && positionOffset == other.positionOffset;
		}

		return false;
	}

	/**
	 * Gets the orientation of the object.
	 *
	 * @return The orientation.
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Gets the position offset of the object.
	 *
	 * @return The position offset.
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

	/**
	 * Gets the type of the object.
	 *
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * positionOffset + orientation;
		return prime * result + type;
	}

	@Override
	public int priority() {
		return HIGH_PRIORITY;
	}

}