package org.apollo.game.message.impl;

import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to spawn an object.
 *
 * @author Major
 */
public final class SendObjectMessage extends RegionUpdateMessage {

	/**
	 * The id of the object.
	 */
	private final int id;

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
	 * Creates the SendObjectMessage.
	 *
	 * @param object The {@link GameObject} to send.
	 * @param positionOffset The offset of the object's position from the region's central position.
	 */
	public SendObjectMessage(GameObject object, int positionOffset) {
		id = object.getId();
		this.positionOffset = positionOffset;
		type = object.getType();
		orientation = object.getOrientation();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SendObjectMessage) {
			SendObjectMessage other = (SendObjectMessage) obj;
			if (id != other.id || type != other.type) {
				return false;
			}

			return positionOffset == other.positionOffset && type == other.type;
		}

		return false;
	}

	/**
	 * Gets the id of the object.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
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
	 * Gets the orientation of the object.
	 *
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * id + orientation;
		result = prime * result + type;
		return prime * result + positionOffset;
	}

	@Override
	public int priority() {
		return LOW_PRIORITY;
	}

}