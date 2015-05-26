package org.apollo.game.model.area.update;

import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.message.impl.RemoveObjectMessage;
import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.obj.GameObject;

/**
 * A {@link UpdateOperation} for addition or removal of {@link GameObject}s.
 *
 * @author Major
 */
public final class ObjectUpdateOperation extends UpdateOperation<GameObject> {

	/**
	 * Creates the ObjectUpdateOperation.
	 *
	 * @param region The {@link Region} in which the ObjectUpdateOperation occurred. Must not be {@code null}.
	 * @param type The {@link EntityUpdateType}. Must not be {@code null}.
	 * @param object The {@linkGameObject}. Must not be {@code null}.
	 */
	public ObjectUpdateOperation(Region region, EntityUpdateType type, GameObject object) {
		super(region, type, object);
	}

	@Override
	protected RegionUpdateMessage add(int offset) {
		return new SendObjectMessage(entity, offset);
	}

	@Override
	protected RegionUpdateMessage remove(int offset) {
		return new RemoveObjectMessage(entity, offset);
	}

}