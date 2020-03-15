package org.apollo.game.fs.decoder;

import org.apollo.util.ThreadUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * A composite decoder that executes each child in parallel.
 *
 * @author Major
 */
public final class SynchronousDecoder {

	/**
	 * The time to wait before cancelling the decoding, in milliseconds.
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
	 * @throws SynchronousDecoderException If a decoder failed to complete successfully.
	 */
	public void block() throws InterruptedException, SynchronousDecoderException {
		List<Future> futures = runnables.stream().map(executor::submit).collect(Collectors.toList());

		executor.shutdown();
		executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);

		for (Future future : futures) {
			try {
				future.get();
			} catch (ExecutionException cause) {
				throw new SynchronousDecoderException("Unable to run all decoder tasks.", cause);
			}
		}
	}

}