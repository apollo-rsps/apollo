package org.apollo.game.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.cache.IndexedFileSystem;
import org.apollo.net.update.HttpRequestWorker;
import org.apollo.net.update.JagGrabRequestWorker;
import org.apollo.net.update.OnDemandRequestWorker;
import org.apollo.net.update.RequestWorker;
import org.apollo.net.update.UpdateDispatcher;

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
		int release = context.getRelease().getReleaseNumber();
		try {
			Path base = Paths.get("data/fs/", Integer.toString(release));

			for (int i = 0; i < THREADS_PER_TYPE; i++) {
				workers.add(new JagGrabRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
				workers.add(new OnDemandRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
				workers.add(new HttpRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
			}
		} catch (FileNotFoundException reason) {
			logger.log(Level.SEVERE, "Unable to find index or data files from the file system.", reason);
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