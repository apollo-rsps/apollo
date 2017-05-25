package org.apollo.game.plugin;

import org.apollo.game.model.World;
import org.apollo.game.plugin.kotlin.KotlinPluginCompiler;
import org.apollo.game.plugin.kotlin.KotlinPluginScript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KotlinPluginEnvironment implements PluginEnvironment, MessageCollector {

	private static final Logger logger = Logger.getLogger(KotlinPluginEnvironment.class.getName());

	private final World world;
	private final KotlinPluginCompiler pluginCompiler;

	private PluginContext context;

	public KotlinPluginEnvironment(World world) {
		this.world = world;
		this.pluginCompiler = new KotlinPluginCompiler(resolveClasspath(), this);
	}

	/**
	 * Resolve the classpath of the current running {@link Thread}.
	 *
	 * @return A {@link List} of {@link File}s pointing to classpath entries.
	 */
	private static List<File> resolveClasspath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (!(classLoader instanceof URLClassLoader)) {
			throw new RuntimeException("Unable to resolve classpath for current ClassLoader");
		}

		URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
		URL[] classpathUrls = urlClassLoader.getURLs();
		List<File> classpath = new ArrayList<>();

		for (URL classpathUrl : classpathUrls) {
			try {
				classpath.add(new File(classpathUrl.toURI()));
			} catch (URISyntaxException e) {
				throw new RuntimeException("URL returned by ClassLoader is invalid");
			}
		}

		return classpath;
	}

	@Override
	public void parse(InputStream is, String name) {
		//@todo - wait until all plugin classes are loading until running constructors?
		try {
			Class<? extends KotlinPluginScript> pluginClass = pluginCompiler.compile(name);
			Constructor<? extends KotlinPluginScript> pluginConstructor = pluginClass.getConstructor(World.class,
				PluginContext.class);

			pluginConstructor.newInstance(world, context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setContext(PluginContext context) {
		this.context = context;
	}

	@Override
	public void clear() {

	}

	@Override
	public void report(@NotNull CompilerMessageSeverity severity, @NotNull String message,
					   @NotNull CompilerMessageLocation location) {
		if (severity.isError()) {
			logger.log(Level.SEVERE, String.format("%s:%s-%s: %s", location.getPath(), location.getLine(),
				location.getColumn(), message));
		}
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

}
