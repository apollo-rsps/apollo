package org.apollo.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apollo.game.model.Mob;

/**
 * A {@link MobRepository} is a repository of {@link Mob}s that are currently active in the game world.
 * 
 * @author Graham
 * @param <T> The type of mob..
 */
public final class MobRepository<T extends Mob> implements Iterable<T> {

	/**
	 * The {@link Iterator} implementation for the {@link MobRepository} class.
	 * 
	 * @author Graham
	 */
	private final class MobRepositoryIterator implements Iterator<T> {

		/**
		 * The current index of this iterator.
		 */
		private int index = 0;

		/**
		 * The previous index of this iterator.
		 */
		private int previousIndex = -1;

		@Override
		public boolean hasNext() {
			for (int i = index; i < mobs.length; i++) {
				if (mobs[i] != null) {
					index = i;
					return true;
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			T mob = null;
			for (int i = index; i < mobs.length; i++) {
				if (mobs[i] != null) {
					mob = (T) mobs[i];
					index = i;
					break;
				}
			}
			if (mob == null) {
				throw new NoSuchElementException("mob does not exist");
			}
			previousIndex = index;
			index++;
			return mob;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void remove() {
			if (previousIndex == -1) {
				throw new IllegalStateException("cannot remove as the repository is empty");
			}
			MobRepository.this.remove((T) mobs[previousIndex]);
			previousIndex = -1;
		}

	}

	/**
	 * The array of mobs in this repository.
	 */
	private final Mob[] mobs;

	/**
	 * The position of the next free index.
	 */
	private int pointer = 0;

	/**
	 * The current size of this repository.
	 */
	private int size = 0;

	/**
	 * Creates a new mob repository with the specified capacity.
	 * 
	 * @param capacity The maximum number of mobs that can be present in the repository.
	 */
	public MobRepository(int capacity) {
		this.mobs = new Mob[capacity];
	}

	/**
	 * Adds a mob to the repository.
	 * 
	 * @param mob The mob to add.
	 * @return {@code true} if the mob was added, {@code false} if the size has reached the capacity of this repository.
	 */
	public boolean add(T mob) {
		if (size == mobs.length) {
			return false;
		}
		int index = -1;
		for (int i = pointer; i < mobs.length; i++) {
			if (mobs[i] == null) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			for (int i = 0; i < pointer; i++) {
				if (mobs[i] == null) {
					index = i;
					break;
				}
			}
		}
		if (index == -1) {
			return false; // shouldn't happen, but just in case
		}
		mobs[index] = mob;
		mob.setIndex(index + 1);
		if (index == mobs.length - 1) {
			pointer = 0;
		} else {
			pointer = index;
		}
		size++;
		return true;
	}

	/**
	 * Gets the capacity of this repository.
	 * 
	 * @return The maximum size of this repository.
	 */
	public int capacity() {
		return mobs.length;
	}

	@Override
	public Iterator<T> iterator() {
		return new MobRepositoryIterator();
	}

	/**
	 * Removes a mob from the repository.
	 * 
	 * @param mob The mob to remove.
	 * @return {@code true} if the mob was removed, {@code false} if it was not (e.g. if it was never added or has been
	 *         removed already).
	 */
	public boolean remove(T mob) {
		int index = mob.getIndex() - 1;
		if (index < 0 || index >= mobs.length) {
			return false;
		}
		if (mobs[index] == mob) {
			mobs[index] = null;
			mob.setIndex(-1);
			size--;
			return true;
		}
		return false;
	}

	/**
	 * Gets the size of this repository.
	 * 
	 * @return The number of mobs in this repository.
	 */
	public int size() {
		return size;
	}

}