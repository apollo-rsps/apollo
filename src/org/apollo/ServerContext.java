package org.apollo;

import org.apollo.net.release.Release;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

/**
 * A {@link ServerContext} is created along with the {@link Server} object. The
 * primary difference is that a reference to the current context should be
 * passed around within the server. The {@link Server} should not be as it
 * allows access to some methods such as
 * {@link Server#bind(java.net.SocketAddress, java.net.SocketAddress, java.net.SocketAddress)}
 * which user scripts/code should not be able to access.
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
	 * The channel group.
	 */
	private final ChannelGroup group = new DefaultChannelGroup();

	/**
	 * Creates a new server context.
	 * @param release The current release.
	 * @param serviceManager The service manager.
	 */
	ServerContext(Release release, ServiceManager serviceManager) {
		this.release = release;
		this.serviceManager = serviceManager;
		this.serviceManager.setContext(this);
	}

	/**
	 * Gets the channel group.
	 * @return The channel group.
	 */
	public ChannelGroup getChannelGroup() {
		return group;
	}

	/**
	 * Gets the current release.
	 * @return The current release.
	 */
	public Release getRelease() {
		return release;
	}

	/**
	 * Gets the service manager.
	 * @return The service manager.
	 */
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	/**
	 * Gets a service. This method is shorthand for
	 * {@code getServiceManager().getService(...)}.
	 * @param <S> The type of service.
	 * @param clazz The service class.
	 * @return The service, or {@code null} if it could not be found.
	 */
	public <S extends Service> S getService(Class<S> clazz) {
		return serviceManager.getService(clazz);
	}

}
