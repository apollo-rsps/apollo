package org.apollo.game.message.impl.encode;

import org.apollo.game.model.Animation;
import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.game.model.entity.obj.ObjectAnimation;

import java.util.Objects;

/**
 * The type Object animation message.
 *
 * @author Khaled Abdeljaber
 */
public class ObjectAnimationMessage extends RegionUpdateMessage {

	private final ObjectAnimation objectAnimation;
	private final int positionOffset;

	/**
	 * Instantiates a new Object anim message.
	 *
	 * @param objectAnimation the object animation
	 * @param positionOffset  the position offset
	 */
	public ObjectAnimationMessage(ObjectAnimation objectAnimation, int positionOffset) {
		this.objectAnimation = objectAnimation;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets object.
	 *
	 * @return the object
	 */
	public GameObject getObject() {
		return objectAnimation.getObject();
	}

	/**
	 * Gets animation.
	 *
	 * @return the animation
	 */
	public Animation getAnimation() {
		return objectAnimation.getAnimation();
	}

	/**
	 * Gets position offset.
	 *
	 * @return the position offset
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

	@Override
	public int priority() {
		return 4;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ObjectAnimationMessage that = (ObjectAnimationMessage) o;
		return positionOffset == that.positionOffset && objectAnimation.equals(that.objectAnimation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectAnimation, positionOffset);
	}
}
