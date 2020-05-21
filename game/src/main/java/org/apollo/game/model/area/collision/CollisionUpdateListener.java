package org.apollo.game.model.area.collision;

import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionListener;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.obj.GameObject;

/**
 * A {@link RegionListener} that listens to object addition/removals and applies the respective {@link CollisionUpdate}.
 */
public final class CollisionUpdateListener implements RegionListener {

	/**
	 * The {@link CollisionManager} to apply updates to.
	 */
	private CollisionManager collisionManager;

	/**
	 * Create a new {@link CollisionUpdateListener}.
	 *
	 * @param collisionManager The {@link CollisionManager} that collision updates will be applied to.
	 */
	public CollisionUpdateListener(CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}

	@Override
	public void execute(Region region, Entity entity, EntityUpdateType type) {
		EntityType entityType = entity.getEntityType();

		if (entityType != EntityType.STATIC_OBJECT && entityType != EntityType.DYNAMIC_OBJECT) {
			return;
		}

		CollisionUpdate.Builder builder = new CollisionUpdate.Builder();
		if (type == EntityUpdateType.ADD) {
			builder.type(CollisionUpdateType.ADDING);
		} else {
			builder.type(CollisionUpdateType.REMOVING);
		}

		builder.object((GameObject) entity);
		collisionManager.apply(builder.build());
	}
}
