package org.apollo;

/**
 * Represents a service that the server provides.
 * 
 * @author Graham
 */
public abstract class Service {

    /**
     * The server context.
     */
    private ServerContext context;

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