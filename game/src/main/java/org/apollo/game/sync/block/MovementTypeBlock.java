package org.apollo.game.sync.block;

import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * The type movement type of a mob.
 *
 * @author Khaled Abdeljaber
 */
public class MovementTypeBlock extends SynchronizationBlock {

	/**
	 * The enum Movement mode.
	 */
	public enum MovementMode {
		/**
		 * Slow movement mode.
		 */
		SLOW(0),
		/**
		 * Walk movement mode.
		 */
		WALK(1),
		/**
		 * Run movement mode.
		 */
		RUN(2),
		/**
		 * Teleport movement mode.
		 */
		TELEPORT(127);

		private final int clientValue;

		MovementMode(int clientValue) {
			this.clientValue = clientValue;
		}

		/**
		 * Gets mode.
		 *
		 * @return the mode
		 */
		public int getClientValue() {
			return clientValue;
		}
	}

	/**
	 * The Mode.
	 */
	public MovementMode mode;

	/**
	 * Instantiates a new Movement type block.
	 *
	 * @param mode the mode
	 */
	MovementTypeBlock(MovementMode mode) {
		this.mode = mode;
	}

	/**
	 * Gets mode.
	 *
	 * @return the mode
	 */
	public MovementMode getMode() {
		return mode;
	}
}
