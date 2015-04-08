package org.apollo.game.sync.block;

import java.util.HashMap;
import java.util.Map;

/**
 * A specialized collection of {@link SynchronizationBlock}s.
 *
 * @author Graham
 */
public final class SynchronizationBlockSet implements Cloneable {

	/**
	 * A {@link Map} of {@link SynchronizationBlock}s.
	 */
	private final Map<Class<? extends SynchronizationBlock>, SynchronizationBlock> blocks = new HashMap<>(8);

	/**
	 * Adds a {@link SynchronizationBlock}.
	 *
	 * @param block The block to add.
	 */
	public void add(SynchronizationBlock block) {
		Class<? extends SynchronizationBlock> clazz = block.getClass();
		blocks.put(clazz, block);
	}

	/**
	 * Clears the set.
	 */
	public void clear() {
		blocks.clear();
	}

	@Override
	public SynchronizationBlockSet clone() {
		SynchronizationBlockSet copy = new SynchronizationBlockSet();
		copy.blocks.putAll(blocks);
		return copy;
	}

	/**
	 * Checks if this set contains the specified {@link SynchronizationBlock}.
	 *
	 * @param clazz The block's class.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(Class<? extends SynchronizationBlock> clazz) {
		return blocks.containsKey(clazz);
	}

	/**
	 * Gets a {@link SynchronizationBlock} from this set.
	 *
	 * @param clazz The block's class.
	 * @return The block.
	 */
	@SuppressWarnings("unchecked")
	public <T extends SynchronizationBlock> T get(Class<T> clazz) {
		return (T) blocks.get(clazz);
	}

	/**
	 * Removes a {@link SynchronizationBlock} from this set.
	 *
	 * @param clazz The block's class.
	 * @return The removed block.
	 */
	@SuppressWarnings("unchecked")
	public <T extends SynchronizationBlock> T remove(Class<? extends SynchronizationBlock> clazz) {
		return (T) blocks.remove(clazz);
	}

	/**
	 * Gets the size of this set.
	 *
	 * @return The size.
	 */
	public int size() {
		return blocks.size();
	}

}