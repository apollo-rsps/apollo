package org.apollo.game.sync.block;

/**
 * The interacting mob {@link SynchronizationBlock}. Both players and npcs can utilise this block.
 * 
 * @author Major
 */
public final class InteractingMobBlock extends SynchronizationBlock {

	/**
	 * The index of the mob.
	 */
	private final int mobIndex;

	/**
	 * Creates the interacting mob block.
	 * 
	 * @param mobIndex The index of the current interacting mob.
	 */
	InteractingMobBlock(int mobIndex) {
		this.mobIndex = mobIndex;
	}

	/**
	 * Gets the interacting mob's index.
	 * 
	 * @return The index.
	 */
	public int getInteractingMobIndex() {
		return mobIndex;
	}

}