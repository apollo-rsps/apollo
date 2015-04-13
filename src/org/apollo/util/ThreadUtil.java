package org.apollo.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * A static utility class which provides ease of use functionality for {@link Thread}s
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ThreadUtil {

	/**
	 * Returns the amount of available processors available to the Java virtual machine.
	 */
	public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

	/**
	 * A {@link Logger} used to debug messages to the console.
	 */
	private static final Logger LOGGER = Logger.getLogger(ThreadUtil.class.getSimpleName());

	/**
	 * The default {@link UncaughtExceptionHandler} which raises an error from the logger with the exception and name of
	 * the specified thread the exception occurred in.
	 */
	private static final UncaughtExceptionHandler DEFAULT_EXCEPTION_HANDLER = (thread, exception) -> LOGGER.log(Level.SEVERE, "Exception occured in thread " + thread.getName(), exception);

	/**
	 * Builds a {@link ThreadFactory} using the specified {@code String} name-format, {@link ThreadPriority} and
	 * {@link UncaughtExceptionHandler}.
	 *
	 * @param name The name-format used when creating threads, may not be {@code null}.
	 * @param priority The priority used when creating threads, may not be {@code null}.
	 * @param handler The {@link UncaughtExceptionHandler} used when creating threads, may not be {@code null}.
	 * @return A new {@link ThreadFactory} from the specified parameters, never {@code null}.
	 */
	public static ThreadFactory build(String name, ThreadPriority priority, UncaughtExceptionHandler handler) {
		Objects.requireNonNull(priority);

		ThreadFactoryBuilder bldr = new ThreadFactoryBuilder();
		bldr.setNameFormat(name);
		bldr.setPriority(priority.getValue());
		bldr.setUncaughtExceptionHandler(handler);
		return bldr.build();
	}

	/**
	 * Builds a {@link ThreadFactory} using the specified {@code String} name-format, {@link ThreadPriority} and the
	 * default {@link UncaughtExceptionHandler}.
	 *
	 * @param name The name-format used when creating threads, may not be {@code null}.
	 * @param priority The priority used when creating threads, may not be {@code null}.
	 * @return A new {@link ThreadFactory} from the specified parameters, never {@code null}.
	 * @see {@link #DEFAULT_EXCEPTION_HANDLER}
	 */
	public static ThreadFactory build(String name, ThreadPriority priority) {
		return build(name, priority, DEFAULT_EXCEPTION_HANDLER);
	}

	/**
	 * Builds a {@link ThreadFactory} using the specified {@code String} name-format, normal thread priority and the
	 * default {@link UncaughtExceptionHandler}.
	 *
	 * @param name The name-format used when creating threads, may not be {@code null}.
	 * @return A new {@link ThreadFactory} from the specified parameters, never {@code null}.
	 * @see {@link #DEFAULT_EXCEPTION_HANDLER}
	 * @see {@link ThreadPriority#NORMAL_PRIORITY}
	 */
	public static ThreadFactory build(String name) {
		return build(name, ThreadPriority.NORMAL_PRIORITY, DEFAULT_EXCEPTION_HANDLER);
	}

	/**
	 * An enumeration representing the priority of a {@link Thread}.
	 *
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	public enum ThreadPriority {

		/**
		 * Represents the minimum priority of a thread.
		 */
		MINIMUM_PRIORITY(1),

		/**
		 * Represents the normal priority of a thread.
		 */
		NORMAL_PRIORITY(5),

		/**
		 * Represents the maximum priority of a thread.
		 */
		MAXIMUM_PRIORITY(10);

		/**
		 * The value of this thread priority.
		 */
		private final int value;

		/**
		 * Constructs a new {@link ThreadPriority} with the specified value.
		 *
		 * @param value The value of this thread priority, must be within the bounds of {@link Thread#MIN_PRIORITY} and
		 *            {@link Thread#MAX_PRIORITY}.
		 */
		private ThreadPriority(int value) {
			// fail-fast for invalid priority values
			checkArgument(value >= Thread.MIN_PRIORITY, "Thread priority (%s) must be >= %s", value, Thread.MIN_PRIORITY);
			checkArgument(value <= Thread.MAX_PRIORITY, "Thread priority (%s) must be <= %s", value, Thread.MAX_PRIORITY);

			this.value = value;
		}

		/**
		 * Returns the value of this thread priority.
		 */
		public final int getValue() {
			return value;
		}
	}

	/**
	 * Prevents the default-public constructor to discourage instantiation of this class.
	 */
	private ThreadUtil() {
	}

}