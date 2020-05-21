package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.encode.ObjectAnimationMessage;
import org.apollo.game.message.impl.encode.RegionUpdateMessage;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.obj.ObjectAnimation;

/**
 * @author Khaled Abdeljaber
 */
public class ObjectAnimationUpdateOperation extends UpdateOperation<ObjectAnimation> {
	/**
	 * Creates the UpdateOperation.
	 *
	 * @param region The region in which the UpdateOperation occurred. Must not be {@code null}.
	 * @param type   The type of {@link EntityUpdateType}. Must not be {@code null}.
	 * @param entity The {@link Entity} being added or removed. Must not be {@code null}.
	 */
	public ObjectAnimationUpdateOperation(Region region, EntityUpdateType type, ObjectAnimation entity) {
		super(region, type, entity);
	}

	@Override
	protected RegionUpdateMessage add(int offset) {
		return new ObjectAnimationMessage(entity, offset);
	}

	@Override
	protected RegionUpdateMessage remove(int offset) {
		throw new IllegalStateException("Object animations cannot be removed.");
	}
}
