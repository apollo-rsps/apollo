package org.apollo.game.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Represents an animation.
 *
 * @author Graham
 */
public final class Animation {

	/**
	 * A special animation which stops the current animation.
	 */
	public static final Animation STOP_ANIMATION = new Animation(-1);

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * Creates a new animation with no delay.
	 *
	 * @param id The id.
	 */
	public Animation(int id) {
		this(id, 0);
	}

	/**
	 * Creates a new animation.
	 *
	 * @param id The id.
	 * @param delay The delay.
	 */
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Gets the animation's delay.
	 *
	 * @return The animation's delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the animation's id.
	 *
	 * @return The animation's id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Animation animation = (Animation) o;
		return delay == animation.delay && id == animation.id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(delay, id);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("delay", delay)
			.add("id", id)
			.toString();
	}
}