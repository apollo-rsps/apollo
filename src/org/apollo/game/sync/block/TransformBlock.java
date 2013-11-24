package org.apollo.game.sync.block;

/**
 * The transform {@link SynchronizationBlock}. This is an npc-only block that updates the npc's definition in the
 * client, and thus its animations, size, etc.
 * 
 * @author Major
 */
public final class TransformBlock extends SynchronizationBlock {

	/**
	 * The new id.
	 */
	private final int id;

	/**
	 * Creates a new transform block.
	 * 
	 * @param id The id.
	 */
	TransformBlock(int id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

}