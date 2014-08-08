package org.apollo.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apollo.game.model.World;
import org.jruby.embed.ScriptingContainer;

/**
 * A {@link PluginEnvironment} which uses Ruby.
 * 
 * @author Graham
 */
public final class RubyPluginEnvironment implements PluginEnvironment {

	/**
	 * The scripting container.
	 */
	private final ScriptingContainer container = new ScriptingContainer();

	/**
	 * Creates and bootstraps the Ruby plugin environment.
	 * 
	 * @throws IOException If an I/O error occurs during bootstrapping.
	 */
	public RubyPluginEnvironment() throws IOException {
		parseBootstrapper();
	}

	@Override
	public void parse(InputStream is, String name) {
		try {
			container.runScriptlet(is, name);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error parsing scriptlet " + name + ".");
		}
	}

	/**
	 * Parses the bootstrapper.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	private void parseBootstrapper() throws IOException {
		File bootstrap = new File("./data/plugins/bootstrap.rb");
		try (InputStream is = new FileInputStream(bootstrap)) {
			parse(is, bootstrap.getAbsolutePath());
		}
	}

	@Override
	public void setContext(PluginContext context) {
		container.put("$ctx", context);
		container.put("$world", World.getWorld());
	}

}