package org.apollo.net.codec.update;

/**
 * Represents a single 'on-demand' request.
 *
 * @author Graham
 */
public final class OnDemandRequest implements Comparable<OnDemandRequest> {

	/**
	 * An enumeration containing the different request priorities.
	 */
	public enum Priority {

		/**
		 * High priority - used when a player is in-game and data is required immediately.
		 */
		HIGH(0),

		/**
		 * Medium priority - used while loading extra resources when the client is not logged in.
		 */
		MEDIUM(1),

		/**
		 * Low priority - used when a file is not required urgently (such as when serving the rest of the cache whilst
		 * the player is in-game).
		 */
		LOW(2);

		/**
		 * Converts the integer value to a Priority.
		 *
		 * @param value The integer value.
		 * @return The priority.
		 * @throws IllegalArgumentException If the value is outside of the range 1-3 inclusive.
		 */
		public static Priority valueOf(int value) {
			switch (value) {
				case 0:
					return HIGH;
				case 1:
					return MEDIUM;
				case 2:
					return LOW;
				default:
					throw new IllegalArgumentException("Priority out of range - received " + value + ".");
			}
		}

		/**
		 * The integer value.
		 */
		private final int value;

		/**
		 * Creates the Priority.
		 *
		 * @param value The integer value.
		 */
		private Priority(int value) {
			this.value = value;
		}

		/**
		 * Compares this Priority with the specified other Priority.
		 * <p>
		 * Used as an ordinal-independent variant of {@link #compareTo}.
		 *
		 * @param other The other Priority.
		 * @return 1 if this Priority is greater than {@code other}, 0 if they are equal, otherwise -1.
		 */
		public int compareWith(Priority other) {
			return Integer.compare(value, other.value);
		}

		/**
		 * Converts the priority to an integer.
		 *
		 * @return The integer value.
		 */
		public int toInteger() {
			return value;
		}

	}

	/**
	 * The FileDescriptor.
	 */
	private final int fs;
	private final int folder;

	/**
	 * The request Priority.
	 */
	private final Priority priority;

	/**
	 * Creates the OnDemandRequest.
	 *
	 * @param fs       The file system.
	 * @param folder   The folder.
	 * @param priority The {@link Priority}.
	 */
	public OnDemandRequest(int fs, int folder, Priority priority) {
		this.fs = fs;
		this.folder = folder;
		this.priority = priority;
	}

	@Override
	public int compareTo(OnDemandRequest other) {
		return priority.compareWith(other.priority);
	}

	public int getFs() {
		return fs;
	}

	public int getFolder() {
		return folder;
	}

	/**
	 * Gets the {@link Priority}.
	 *
	 * @return The Priority.
	 */
	public Priority getPriority() {
		return priority;
	}

}