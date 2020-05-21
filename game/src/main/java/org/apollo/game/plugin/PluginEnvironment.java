package org.apollo.game.plugin;

import java.util.Collection;
import java.util.Set;

/**
 * Represents some sort of environment that plugins could be executed in, e.g. {@code javax.script} or Jython.
 *
 * @author Graham
 */
public interface PluginEnvironment {

	/**
	 * Load all of the plugins defined in the given {@link Set} of {@link PluginMetaData}.
	 *
	 * @param plugins The plugins to be loaded.
	 */
	void load(Collection<PluginMetaData> plugins);

	/**
	 * Sets the context for this environment.
	 *
	 * @param context The context.
	 */
	public void setContext(PluginContext context);

}