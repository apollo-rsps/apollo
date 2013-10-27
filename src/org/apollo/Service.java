package org.apollo;

/**
 * Represents a service that the server provides.
 * @author Graham
 */
public abstract class Service {

	/**
	 * The server context.
	 */
	private ServerContext ctx;

	/**
	 * Gets the server context.
	 * @return The context.
	 */
	public final ServerContext getContext() {
		return ctx;
	}

	/**
	 * Sets the server context.
	 * @param ctx The context.
	 */
	public final void setContext(ServerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Starts the service.
	 */
	public abstract void start();

}
