package org.apollo.game.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apollo.game.model.World;
import org.apollo.game.plugin.kotlin.KotlinPluginCompiler;
import org.apollo.game.plugin.kotlin.KotlinPluginScript;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation;
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity;
import org.jetbrains.kotlin.cli.common.messages.MessageCollector;

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
	public void load(Collection<PluginMetaData> plugins) {
		try (InputStream resource = KotlinPluginEnvironment.class.getResourceAsStream("/manifest.txt")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
			List<String> pluginClassNames = reader.lines().collect(Collectors.toList());

			for (String pluginClassName : pluginClassNames) {
				Class<? extends KotlinPluginScript> pluginClass =
					(Class<? extends KotlinPluginScript>) Class.forName(pluginClassName);

				Constructor<? extends KotlinPluginScript> pluginConstructor =
					pluginClass.getConstructor(World.class, PluginContext.class);

				KotlinPluginScript plugin = pluginConstructor.newInstance(world, context);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		List<Class<? extends KotlinPluginScript>> pluginClasses = new ArrayList<>();
		List<String> sourceRoots = new ArrayList<>();

		for (PluginMetaData plugin : plugins) {
			List<String> pluginSourceRoots = Arrays.stream(plugin.getScripts())
				.map(script -> plugin.getBase() + "/" + script)
				.collect(Collectors.toList());

			sourceRoots.addAll(pluginSourceRoots);
		}

		for (String scriptSource : sourceRoots) {
//			try {
			List<String> dependencySourceRoots = new ArrayList<>(sourceRoots);
			dependencySourceRoots.remove(scriptSource);

//				pluginClasses.add(pluginCompiler.compile(scriptSource));
//			} catch (KotlinPluginCompilerException e) {
//				throw new RuntimeException(e);
//			}
		}

		for (Class<? extends KotlinPluginScript> pluginClass : pluginClasses) {
			try {
				Constructor<? extends KotlinPluginScript> constructor = pluginClass
					.getConstructor(World.class, PluginContext.class);

				KotlinPluginScript script = constructor.newInstance(world, context);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
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
