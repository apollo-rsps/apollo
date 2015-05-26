package org.apollo;

import java.util.Objects;

import org.apollo.cache.IndexedFileSystem;
import org.apollo.game.service.GameService;
import org.apollo.game.service.LoginService;
import org.apollo.game.service.UpdateService;
import org.apollo.net.release.Release;

/**
 * A {@link ServerContext} is created along with the {@link Server} object. The primary difference is that a reference
 * to the current context should be passed around within the server. The {@link Server} should not be as it allows
 * access to some methods such as {@link Server#bind} which user scripts/code should not be able to access.
 *
 * @author Graham
 * @author Major
 */
public final class ServerContext {

	/**
	 * The IndexedFileSystem.
	 */
	private final IndexedFileSystem fileSystem;

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * The service manager.
	 */
	private final ServiceManager services;

	/**
	 * Creates a new server context.
	 *
	 * @param release The current release.
	 * @param services The service manager.
	 * @param fileSystem The indexed file system.
	 */
	protected ServerContext(Release release, ServiceManager services, IndexedFileSystem fileSystem) {
		this.release = Objects.requireNonNull(release);
		this.services = Objects.requireNonNull(services);
		this.services.setContext(this);
		this.fileSystem = Objects.requireNonNull(fileSystem);
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
	 * Gets the {@link GameService}.
	 *
	 * @return The GameService.
	 */
	public GameService getGameService() {
		return services.getGame();
	}

	/**
	 * Gets the {@link LoginService}.
	 *
	 * @return The LoginService.
	 */
	public LoginService getLoginService() {
		return services.getLogin();
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
	 * Gets the {@link UpdateService}.
	 *
	 * @return The UpdateService.
	 */
	public UpdateService getUpdateService() {
		return services.getUpdate();
	}

}