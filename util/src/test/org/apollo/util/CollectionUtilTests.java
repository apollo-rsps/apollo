package org.apollo.util;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

import static org.junit.Assert.assertTrue;

/**
 * Contains unit tests for {@link CollectionUtil}s.
 *
 * @author Major
 */
public final class CollectionUtilTests {

	/**
	 * Tests that {@link CollectionUtil#pollAll} functions correctly.
	 */
	@Test
	public void poll() {
		Queue<String> queue = new ArrayDeque<>(Arrays.asList("hello", "world"));
		CollectionUtil.pollAll(queue, String::length);
		assertTrue(queue.isEmpty());
	}

}
