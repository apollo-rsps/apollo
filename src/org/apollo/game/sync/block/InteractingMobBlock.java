package org.apollo.game.sync.block;

/**
 * The interacting mob {@link SynchronizationBlock}. Both players and npcs can utilise this block.
 *
 * @author Major
 */
public final class InteractingMobBlock extends SynchronizationBlock {

	/**
	 * The index used to reset the interacting mob.
	 */
	public static final int RESET_INDEX = 65_535;

	/**
	 * The index of the mob.
	 */
	private final int index;

	/**
	 * Creates the interacting mob block.
	 *
	 * @param index The index of the current interacting mob.
	 */
	InteractingMobBlock(int index) {
		this.index = index;
	}

	/**
	 * Gets the interacting mob's index.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

}