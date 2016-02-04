package org.apollo.game.model.entity;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.base.Preconditions;

/**
 * A {@link MobRepository} is a repository of {@link Mob}s that are currently active in the game world.
 *
 * @param <T> The type of Mob.
 * @author Graham
 * @author Ryley
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
		 * The current index of this Iterator.
		 */
		private int current;

		/**
		 * The last index found.
		 */
		private int last = -1;

		/**
		 * The repository of {@link Mob}s this {@link Iterator} iterates over.
		 */
		private final MobRepository<T> repository;

		/**
		 * Constructs a new {@link MobRepositoryIterator} with the specified
		 * MobRepository.
		 * 
		 * @param repository
		 *            The MobRepository we're iterating over.
		 */
		private MobRepositoryIterator(MobRepository<T> repository) {
			this.repository = repository;
		}

		@Override
		public boolean hasNext() {
			int index = current;

			while (index <= repository.size()) {
				Mob mob = repository.mobs[index++];
				if (mob != null) {
					return true;
				}
			}

			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			while (current <= repository.size()) {
				Mob mob = repository.mobs[current++];
				if (mob != null) {
					last = current;
					return (T) mob;
				}
			}

			throw new NoSuchElementException("There are no more elements!");
		}

		@Override
		public void remove() {
			if (last == -1) {
				throw new IllegalStateException("remove() may only be called once per call to next()");
			}

			repository.remove(last);
			last = -1;
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
	 * @return {@code true} if the Mob was added, {@code false} if this MobRepository is at maximum capacity.
	 */
	public boolean add(T mob) {
		if (full()) {
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
	 * Returns whether or not this MobRepository has reached its maxmimum capacity.
	 *
	 * @return {@code true} iff this MobRepository is full.
	 */
	public boolean full() {
		return size == mobs.length;
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
	 */
	public void remove(T mob) {
		Preconditions.checkNotNull(mob, "Mob may not be null.");
		remove(mob.getIndex());
	}

	/**
	 * Removes a Mob from the repository by the specified index.
	 *
	 * @param index The index of the Mob to remove.
	 */
	private void remove(int index) {
		Mob mob = get(index);

		if (mob.getIndex() != index) {
			throw new IllegalArgumentException("MobRepository index mismatch, cannot remove Mob.");
		}

		mobs[index - 1] = null;
		mob.setIndex(-1);
		size--;
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