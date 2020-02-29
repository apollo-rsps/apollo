package org.apollo.game.service;

import org.apollo.Service;
import org.apollo.cache.Cache;
import org.apollo.net.update.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * A class which services file requests.
 *
 * @author Graham
 */
public final class UpdateService extends Service {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(UpdateService.class.getName());

	/**
	 * The number of request types.
	 */
	private static final int REQUEST_TYPES = 3;

	/**
	 * The number of threads per request type.
	 */
	private static final int THREADS_PER_TYPE = Runtime.getRuntime().availableProcessors();

	/**
	 * The UpdateDispatcher.
	 */
	private final UpdateDispatcher dispatcher = new UpdateDispatcher();

	/**
	 * The ExecutorService.
	 */
	private final ExecutorService service = Executors.newFixedThreadPool(REQUEST_TYPES * THREADS_PER_TYPE);

	/**
	 * The List of RequestWorkers.
	 */
	private final List<RequestWorker<?, ?>> workers = new ArrayList<>();

	/**
	 * Gets the update dispatcher.
	 *
	 * @return The update dispatcher.
	 */
	public UpdateDispatcher getDispatcher() {
		return dispatcher;
	}

	@Override
	public void start() {
		Path base = Paths.get("data/fs/", Integer.toString(context.getRelease().getReleaseNumber()));
		final var cache = Cache.openCache(base);
		for (int i = 0; i < THREADS_PER_TYPE; i++) {
			workers.add(new JagGrabRequestWorker(dispatcher, cache));
			workers.add(new OnDemandRequestWorker(dispatcher, cache));
			workers.add(new HttpRequestWorker(dispatcher, cache));
		}

		workers.forEach(service::submit);
	}

	/**
	 * Stops the threads in the pool.
	 */
	public void stop() {
		workers.forEach(RequestWorker::stop);
		service.shutdownNow();
	}

}