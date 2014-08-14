package org.apollo.update;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apollo.Service;
import org.apollo.fs.IndexedFileSystem;

/**
 * A class which services file requests.
 * 
 * @author Graham
 */
public final class UpdateService extends Service {

	/**
	 * The number of request types.
	 */
	private static final int REQUEST_TYPES = 3;

	/**
	 * The number of threads per request type.
	 */
	private static final int THREADS_PER_REQUEST_TYPE = Runtime.getRuntime().availableProcessors();

	/**
	 * The update dispatcher.
	 */
	private final UpdateDispatcher dispatcher = new UpdateDispatcher();

	/**
	 * The executor service.
	 */
	private final ExecutorService service;

	/**
	 * A list of request workers.
	 */
	private final List<RequestWorker<?, ?>> workers = new ArrayList<>();

	/**
	 * Creates the update service.
	 */
	public UpdateService() {
		int totalThreads = REQUEST_TYPES * THREADS_PER_REQUEST_TYPE;
		service = Executors.newFixedThreadPool(totalThreads);
	}

	/**
	 * Gets the update dispatcher.
	 * 
	 * @return The update dispatcher.
	 */
	public UpdateDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Starts the threads in the pool.
	 */
	@Override
	public void start() {
		int release = getContext().getRelease().getReleaseNumber();
		try {
			Path base = Paths.get("data/fs/", Integer.toString(release));
			for (int i = 0; i < THREADS_PER_REQUEST_TYPE; i++) {
				workers.add(new JagGrabRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
				workers.add(new OnDemandRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
				workers.add(new HttpRequestWorker(dispatcher, new IndexedFileSystem(base, true)));
			}

			for (RequestWorker<?, ?> worker : workers) {
				service.submit(worker);
			}
		} catch (Exception ex) {
			System.err.println("Error adding request workers - " + ex.getMessage());
		}
	}

	/**
	 * Stops the threads in the pool.
	 */
	public void stop() {
		for (RequestWorker<?, ?> worker : workers) {
			worker.stop();
		}

		service.shutdownNow();
	}

}