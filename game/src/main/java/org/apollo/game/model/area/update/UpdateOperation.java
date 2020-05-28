package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.entity.Entity;
import org.apollo.net.message.Message;

import com.google.common.base.Preconditions;

/**
 * An type that is contained in the snapshot of a {@link Region}, which consists of an {@link Entity} being added,
 * removed, or moved.
 *
 * @param <E> The type of {@link Entity} in this type.
 * @author Major
 */
public abstract class UpdateOperation<E extends Entity> {

	/**
	 * The Entity involved in this UpdateOperation.
	 */
	protected final E entity;

	/**
	 * The Region in which this type occurred.
	 */
	protected final Region region;

	/**
	 * The type of update.
	 */
	protected final EntityUpdateType type;

	/**
	 * Creates the UpdateOperation.
	 *
	 * @param region The region in which the UpdateOperation occurred. Must not be {@code null}.
	 * @param type The type of {@link EntityUpdateType}. Must not be {@code null}.
	 * @param entity The {@link Entity} being added or removed. Must not be {@code null}.
	 */
	public UpdateOperation(Region region, EntityUpdateType type, E entity) {
		this.region = region;
		this.type = type;
		this.entity = entity;
	}

	/**
	 * Gets a {@link RegionUpdateMessage} that would counteract the effect of this UpdateOperation.
	 *
	 * @return The RegionUpdateMessage.
	 */
	public final RegionUpdateMessage inverse() {
		int offset = region.getPositionOffset(entity);

		switch (type) {
			case ADD:
				return remove(offset);
			case REMOVE:
				return add(offset);
			default:
				throw new IllegalStateException("Unsupported EntityUpdateType " + type + ".");
		}
	}

	/**
	 * Returns this UpdateOperation as a {@link Message}.
	 *
	 * @return The Message.
	 */
	public final RegionUpdateMessage toMessage() {
		int offset = region.getPositionOffset(entity);

		switch (type) {
			case ADD:
				return add(offset);
			case REMOVE:
				return remove(offset);
			default:
				throw new IllegalStateException("Unsupported EntityUpdateType " + type + ".");
		}
	}

	/**
	 * Returns a {@link RegionUpdateMessage} that adds the {@link Entity} in this UpdateOperation.
	 *
	 * @param offset The offset of the {@link Position} of the Entity from the Position of the {@link Region}.
	 * @return The RegionUpdateMessage.
	 */
	protected abstract RegionUpdateMessage add(int offset);

	/**
	 * Returns a {@link RegionUpdateMessage} that removes the {@link Entity} in this UpdateOperation.
	 *
	 * @param offset The offset of the {@link Position} of the Entity from the Position of the {@link Region}.
	 * @return The RegionUpdateMessage.
	 */
	protected abstract RegionUpdateMessage remove(int offset);

}