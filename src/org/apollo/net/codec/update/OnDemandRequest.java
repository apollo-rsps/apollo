package org.apollo.net.codec.update;

import org.apollo.fs.FileDescriptor;

/**
 * Represents a single 'on-demand' request.
 * 
 * @author Graham
 */
public final class OnDemandRequest implements Comparable<OnDemandRequest> {

	/**
	 * An enumeration containing the different request priorities.
	 * 
	 * @author Graham
	 */
	public enum Priority {

		/**
		 * High priority - used in-game when data is required immediately but has not yet been received.
		 */
		HIGH(0),

		/**
		 * Medium priority - used while loading the 'bare minimum' required to run the game.
		 */
		MEDIUM(1),

		/**
		 * Low priority - used when a file is not required urgently. The client login screen says
		 * "loading extra files.." when low priority loading is being performed.
		 */
		LOW(2);

		/**
		 * Converts the integer value to a priority.
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
		 * Creates a priority.
		 * 
		 * @param value The integer value.
		 */
		private Priority(int value) {
			this.value = value;
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
	 * The file descriptor.
	 */
	private final FileDescriptor fileDescriptor;

	/**
	 * The request priority.
	 */
	private final Priority priority;

	/**
	 * Creates the 'on-demand' request.
	 * 
	 * @param fileDescriptor The file descriptor.
	 * @param priority The priority.
	 */
	public OnDemandRequest(FileDescriptor fileDescriptor, Priority priority) {
		this.fileDescriptor = fileDescriptor;
		this.priority = priority;
	}

	@Override
	public int compareTo(OnDemandRequest other) {
		int thisPriority = priority.toInteger();
		int otherPriority = other.priority.toInteger();

		if (thisPriority < otherPriority) {
			return 1;
		} else if (thisPriority == otherPriority) {
			return 0;
		}
		return -1;
	}

	/**
	 * Gets the file descriptor.
	 * 
	 * @return The file descriptor.
	 */
	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return The priority.
	 */
	public Priority getPriority() {
		return priority;
	}

}