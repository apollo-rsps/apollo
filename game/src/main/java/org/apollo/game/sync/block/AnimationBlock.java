package org.apollo.game.sync.block;

import org.apollo.game.model.Animation;

/**
 * The animation {@link SynchronizationBlock}. Both npcs and players can utilise this block.
 *
 * @author Graham
 */
public final class AnimationBlock extends SynchronizationBlock {

	/**
	 * The animation.
	 */
	private final Animation animation;

	/**
	 * Creates the animation block.
	 *
	 * @param animation The animation.
	 */
	AnimationBlock(Animation animation) {
		this.animation = animation;
	}

	/**
	 * Gets the {@link Animation}.
	 *
	 * @return The animation.
	 */
	public Animation getAnimation() {
		return animation;
	}

}