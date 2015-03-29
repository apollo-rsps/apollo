package org.apollo.util;

import java.util.Iterator;

import org.apollo.game.model.entity.Mob;

import com.google.common.base.Preconditions;

/**
 * A {@link MobRepository} is a repository of {@link Mob}s that are currently active in the game world.
 * 
 * @author Graham
 * @author Ryley
 * @param <T> The type of Mob.
 */
public final class MobRepository<T extends Mob> implements Iterable<T> {

	/**
	 * The {@link Iterator} implementation for the MobRepository.
	 * 
	 * @author Graham
	 * @author Ryley
	 */
	private final class MobRepositoryIterator implements Iterator<T> {

		/**
		 * The repository of {@link Mob}s this {@link Iterator} iterates over.
		 */
		private final MobRepository<T> repository;

		/**
		 * The current index of this iterator.
		 */
		private int currentIndex;

		/**
		 * The amount of indexes found.
		 */
		private int foundIndex;

		/**
		 * Constructs a new {@link MobRepositoryIterator} with the specified MobRepository.
		 * 
		 * @param repository The repository of Mob's this Iterator iterates over.
		 */
		private MobRepositoryIterator(MobRepository<T> repository) {
			this.repository = repository;
		}

		@Override
		public boolean hasNext() {
			if (foundIndex == size()) {
				return false;
			}

			while (currentIndex < capacity()) {
				if (mobs[currentIndex++] != null) {
					foundIndex++;
					return true;
				}
			}
			return false;
		}

		@Override
		public T next() {
			return get(currentIndex);
		}

		@Override
		public void remove() {
			repository.remove(currentIndex + 1);
		}

	}

	/**
	 * The array of Mobs in this repository.
	 */
	private final Mob[] mobs;

	/**
	 * The current size of this repository.
	 */
	private int size = 0;

	/**
	 * Creates a new Mob repository with the specified capacity.
	 * 
	 * @param capacity The maximum number of Mobs that can be present in the repository.
	 */
	public MobRepository(int capacity) {
		mobs = new Mob[capacity];
	}

	/**
	 * Adds a Mob to the repository.
	 * 
	 * @param mob The Mob to add.
	 * @return {@code true} if the Mob was added, {@code false} if the size has reached the capacity of this repository.
	 */
	public boolean add(T mob) {
		if (size == capacity()) {
			return false;
		}

		for (int index = 0; index < capacity(); index++) {
			if (mobs[index] != null) {
				continue;
			}

			mobs[index] = mob;
			mob.setIndex(index + 1);
			size++;

			return true;
		}

		return false;
	}

	/**
	 * Gets the capacity of this repository.
	 * 
	 * @return The maximum size of this repository.
	 */
	public int capacity() {
		return mobs.length;
	}

	/**
	 * Gets the Mob at the given index.
	 *
	 * @param index The index of the Mob.
	 * @return The Mob instance.
	 * @throws IndexOutOfBoundsException If the specified index is not [0, capacity).
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		if (index < 1 || index >= capacity() + 1) {
			throw new IndexOutOfBoundsException("Mob index is out of bounds.");
		}
		return (T) mobs[index - 1];
	}

	@Override
	public Iterator<T> iterator() {
		return new MobRepositoryIterator(this);
	}

	/**
	 * Removes a Mob from the repository.
	 * 
	 * @param mob The Mob to remove.
	 * @return {@code true} if the Mob was removed, {@code false} if not.
	 */
	public boolean remove(T mob) {
		Preconditions.checkNotNull(mob);
		return remove(mob.getIndex());
	}

	/**
	 * Removes a Mob from the repository by the specified index.
	 * 
	 * @param index The index of the Mob to remove.
	 * @return {@code true} if the Mob at the specified index was removed otherwise {@code false}.
	 */
	public boolean remove(int index) {
		Mob mob = get(index);

		if (mob.getIndex() != index) {
			return false;
		}

		mobs[index - 1] = null;
		mob.setIndex(-1);
		size--;
		return true;
	}

	/**
	 * Gets the size of this repository.
	 * 
	 * @return The number of Mobs in this repository.
	 */
	public int size() {
		return size;
	}

}