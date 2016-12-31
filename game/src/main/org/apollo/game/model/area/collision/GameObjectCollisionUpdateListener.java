package org.apollo.game.model.area.collision;

import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionListener;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.obj.GameObject;

/**
 * A {@link RegionListener} which listens on object addition / removal events and applies
 * the respective {@link CollisionUpdate}.
 */
public final class GameObjectCollisionUpdateListener implements RegionListener {
	/**
	 * The {@link CollisionManager} to apply updates to.
	 */
	private CollisionManager collisionManager;

	/**
	 * Create a new {@link GameObjectCollisionUpdateListener}.
	 *
	 * @param collisionManager The {@link CollisionManager} that collision updates will be applied to.
	 */
	public GameObjectCollisionUpdateListener(CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}

	@Override
	public void execute(Region region, Entity entity, EntityUpdateType type) {
		EntityType entityType = entity.getEntityType();

		if (entityType != EntityType.STATIC_OBJECT && entityType != EntityType.DYNAMIC_OBJECT) {
			return;
		}

		CollisionUpdate.Builder objectUpdateBuilder = new CollisionUpdate.Builder();
		if (type == EntityUpdateType.ADD) {
			objectUpdateBuilder.type(CollisionUpdateType.ADDING);
		} else {
			objectUpdateBuilder.type(CollisionUpdateType.REMOVING);
		}

		objectUpdateBuilder.object((GameObject) entity);

		CollisionUpdate objectUpdate = objectUpdateBuilder.build();
		collisionManager.apply(objectUpdate);
	}
}
