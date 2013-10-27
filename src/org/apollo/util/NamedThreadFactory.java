package org.apollo.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link ThreadFactory} which gives each thread a unique name made up of the
 * name supplied in the constructor and postfixed with an id.
 * <p>
 * For example, if the name {@code MyThread} was given and a third thread was
 * created by the factory, the resulting name would be {@code MyThread [id=2]}.
 * @author Graham
 */
public final class NamedThreadFactory implements ThreadFactory {

	/**
	 * The unique name.
	 */
	private final String name;

	/**
	 * The next id.
	 */
	private AtomicInteger id = new AtomicInteger(0);

	/**
	 * Creates the named thread factory.
	 * @param name The unique name.
	 */
	public NamedThreadFactory(String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		int currentId = id.getAndIncrement();
		return new Thread(runnable, name + " [id=" + currentId + "]");
	}

}
