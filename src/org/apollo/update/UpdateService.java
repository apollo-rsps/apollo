package org.apollo.update;

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
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(UpdateService.class.getName());

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