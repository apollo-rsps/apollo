package org.apollo.game.sync.block;

import org.apollo.game.model.Animation;

/**
 * The animation {@link SynchronizationBlock}.
 * @author Graham
 */
public final class AnimationBlock extends SynchronizationBlock {

	/**
	 * The animation.
	 */
	private final Animation animation;

	/**
	 * Creates the animation block.
	 * @param animation The animation.
	 */
	AnimationBlock(Animation animation) {
		this.animation = animation;
	}

	/**
	 * Gets the animation.
	 * @return The animation.
	 */
	public Animation getAnimation() {
		return animation;
	}

}
