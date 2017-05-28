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

public class KotlinPluginEnvironment implements PluginEnvironment {

	private static final Logger logger = Logger.getLogger(KotlinPluginEnvironment.class.getName());

	private final World world;
	private PluginContext context;

	public KotlinPluginEnvironment(World world) {
		this.world = world;
	}

	@Override
	public void load(Collection<PluginMetaData> plugins) {
		List<KotlinPluginScript> pluginScripts = new ArrayList<>();

		try (InputStream resource = KotlinPluginEnvironment.class.getResourceAsStream("/manifest.txt")) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
			List<String> pluginClassNames = reader.lines().collect(Collectors.toList());

			for (String pluginClassName : pluginClassNames) {
				Class<? extends KotlinPluginScript> pluginClass =
					(Class<? extends KotlinPluginScript>) Class.forName(pluginClassName);

				Constructor<? extends KotlinPluginScript> pluginConstructor =
					pluginClass.getConstructor(World.class, PluginContext.class);

				pluginScripts.add(pluginConstructor.newInstance(world, context));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		pluginScripts.forEach(KotlinPluginScript::doStart);
	}

	@Override
	public void setContext(PluginContext context) {
		this.context = context;
	}

}
