package org.apollo.game.model.entity.obj;

import org.apollo.game.model.Animation;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.ObjectAnimationUpdateOperation;
import org.apollo.game.model.area.update.UpdateOperation;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;

import java.util.Objects;

/**
 * @author Khaled Abdeljaber
 */
public class ObjectAnimation extends Entity implements GroupableEntity {

	private final GameObject object;
	private final Animation animation;

	public ObjectAnimation(World world, Position position, GameObject object, Animation animation) {
		super(world, position);
		this.object = object;
		this.animation = animation;
	}

	public GameObject getObject() {
		return object;
	}

	public Animation getAnimation() {
		return animation;
	}

	@Override
	public UpdateOperation<?> toUpdateOperation(Region region, EntityUpdateType type) {
		return new ObjectAnimationUpdateOperation(region, type, this);
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.OBJECT_ANIMATION;
	}

	@Override
	public int getLength() {
		return object.getLength();
	}

	@Override
	public int getWidth() {
		return object.getWidth();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObjectAnimation that = (ObjectAnimation) o;
		return object.equals(that.object) && animation.equals(that.animation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(object, animation);
	}
}
