package org.apollo;

import org.apollo.game.model.World;

/**
 * Represents a service that the server provides for a {@link World}.
 * 
 * @author Graham
 */
public abstract class Service {

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The server context.
	 */
	private ServerContext context;

	/**
	 * Creates the Service.
	 *
	 * @param world The {@link World} the Service is for.
	 */
	public Service(World world) {
		this.world = world;
	}

	/**
	 * Gets the {@link ServerContext}.
	 * 
	 * @return The context.
	 */
	public final ServerContext getContext() {
		return context;
	}

	/**
	 * Sets the {@link ServerContext}.
	 * 
	 * @param context The context.
	 */
	public final void setContext(ServerContext context) {
		this.context = context;
	}

	/**
	 * Starts the service.
	 */
	public abstract void start();

}