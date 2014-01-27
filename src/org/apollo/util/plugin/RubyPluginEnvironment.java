package org.apollo.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		container.runScriptlet(is, name);
	}

	/**
	 * Parses the bootstrapper.
	 * 
	 * @throws IOException If an I/O error occurs.
	 */
	private void parseBootstrapper() throws IOException {
		File bootstrap = new File("./data/plugins/bootstrap.rb");
		InputStream is = new FileInputStream(bootstrap);
		try {
			parse(is, bootstrap.getAbsolutePath());
		} finally {
			is.close();
		}
	}

	@Override
	public void setContext(PluginContext context) {
		container.put("$ctx", context);
	}

}