package org.apollo.game.sync.block;

/**
 * The interacting mob {@link SynchronizationBlock}.
 * <p>
 * Note: As all Apollo events should be immutable to avoid concurrency issues, this uses the index of the mob rather
 * than the actual mob. This should not be changed.
 * 
 * @author Major
 */
public class InteractingMobBlock extends SynchronizationBlock {

	/**
	 * The index of the mob.
	 */
	private final int mobIndex;

	/**
	 * Creates the interacting mob block.
	 * 
	 * @param mobIndex The index of the current interacting mob.
	 */
	public InteractingMobBlock(int mobIndex) {
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