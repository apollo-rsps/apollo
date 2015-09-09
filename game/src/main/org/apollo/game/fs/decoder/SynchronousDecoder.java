package org.apollo.game.fs.decoder;

import org.apollo.util.ThreadUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A composite decoder that executes each child in parallel.
 *
 * @author Major
 */
public final class SynchronousDecoder {

	/**
	 * The time to wait before cancelling the decoding.
	 */
	private static final int TIMEOUT = 15_000;

	/**
	 * The Executor used to execute the Runnable(s).
	 */
	private final ExecutorService executor = Executors.newFixedThreadPool(ThreadUtil.AVAILABLE_PROCESSORS,
			ThreadUtil.create("SynchronousDecoder"));

	/**
	 * The List of Runnables.
	 */
	private final List<Runnable> runnables;

	/**
	 * Creates the SynchronousDecoder.
	 *
	 * @param runnables The {@link Runnable}s to execute.
	 */
	public SynchronousDecoder(Runnable... runnables) {
		this.runnables = Arrays.asList(runnables);
	}

	/**
	 * Starts this SynchronousDecoder.
	 *
	 * @throws InterruptedException If a decoder is still running after {@link #TIMEOUT} ms.
	 */
	public void block() throws InterruptedException {
		runnables.forEach(executor::submit);
		executor.shutdown();
		executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
	}

}