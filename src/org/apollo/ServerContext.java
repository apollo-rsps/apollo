package org.apollo;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.net.release.Release;

/**
 * A {@link ServerContext} is created along with the {@link Server} object. The primary difference is that a reference
 * to the current context should be passed around within the server. The {@link Server} should not be as it allows
 * access to some methods such as {@link Server#bind} which user scripts/code should not be able to access.
 *
 * @author Graham
 */
public final class ServerContext {

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * The service manager.
	 */
	private final ServiceManager serviceManager;

	/**
	 * The IndexedFileSystem.
	 */
	private final IndexedFileSystem fileSystem;

	/**
	 * Creates a new server context.
	 *
	 * @param release The current release.
	 * @param serviceManager The service manager.
	 */
	ServerContext(Release release, ServiceManager serviceManager, IndexedFileSystem fileSystem) {
		this.release = release;
		this.serviceManager = serviceManager;
		this.serviceManager.setContext(this);
		this.fileSystem = fileSystem;
	}

	/**
	 * Gets the current release.
	 *
	 * @return The current release.
	 */
	public Release getRelease() {
		return release;
	}

	/**
	 * Gets the IndexeFileSystem
	 *
	 * @return The IndexedFileSystem.
	 */
	public IndexedFileSystem getFileSystem() {
		return fileSystem;
	}

	/**
	 * Gets a service. This method is shorthand for {@code getServiceManager().getService(...)}.
	 *
	 * @param clazz The service class.
	 * @return The service, or {@code null} if it could not be found.
	 */
	public <S extends Service> S getService(Class<S> clazz) {
		return serviceManager.getService(clazz);
	}

	/**
	 * Gets the service manager.
	 *
	 * @return The service manager.
	 */
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

}