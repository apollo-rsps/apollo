package org.apollo.util;

import org.junit.Test;

/**
 * Contains unit tests for {@link ThreadUtil}s.
 *
 * @author Major
 */
public final class ThreadUtilTests {

	/**
	 * Tests that trying to use an illegal name throws a {@link java.lang.Thread.UncaughtExceptionHandler}.
	 */
	@Test(expected = NullPointerException.class)
	public void badHandler() {
		ThreadUtil.create("factory", 1, null);
	}

	/**
	 * Tests that trying to use an illegal name throws a {@link NullPointerException}.
	 */
	@Test(expected = NullPointerException.class)
	public void badName() {
		ThreadUtil.create(null);
	}

	/**
	 * Tests that using a priority above 10 throws an {@link IllegalArgumentException}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void largePriority() {
		ThreadUtil.create("factory", 11);
	}

	/**
	 * Tests that using a priority below 1 throws an {@link IllegalArgumentException}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void smallPriority() {
		ThreadUtil.create("factory", 0);
	}

}
