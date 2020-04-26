package org.apollo.game.plugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.apollo.game.model.World;
import org.apollo.game.plugin.kotlin.KotlinPluginScript;

public class KotlinPluginEnvironment implements PluginEnvironment {

	private static final Logger logger = Logger.getLogger(KotlinPluginEnvironment.class.getName());
	private static final String PLUGIN_SUFFIX = "_plugin";

	private final World world;
	private PluginContext context;

	public KotlinPluginEnvironment(World world) {
		this.world = world;
	}

	@Override
	public void load(Collection<PluginMetaData> plugins) {
		List<KotlinPluginScript> pluginScripts = new ArrayList<>();

		ClassGraph classGraph = new ClassGraph().enableAllInfo();

		try (ScanResult scanResult = classGraph.scan()) {
			ClassInfoList pluginClassList = scanResult
				.getSubclasses(KotlinPluginScript.class.getName())
				.directOnly();

			for (ClassInfo pluginClassInfo : pluginClassList) {
				Class<KotlinPluginScript> scriptClass = pluginClassInfo.loadClass(KotlinPluginScript.class);
				Constructor<KotlinPluginScript> scriptConstructor = scriptClass.getConstructor(World.class,
					PluginContext.class);

				pluginScripts.add(scriptConstructor.newInstance(world, context));
				logger.info(String.format("Loaded plugin: %s", pluginDescriptor(scriptClass)));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		pluginScripts.forEach(script -> script.doStart(world));
	}

	@Override
	public void setContext(PluginContext context) {
		this.context = context;
	}

	private static String pluginDescriptor(Class<? extends KotlinPluginScript> clazz) {
		String className = clazz.getSimpleName();
		String name = className.substring(0, className.length() - PLUGIN_SUFFIX.length());
		Package pkg = clazz.getPackage();

		return pkg == null ? name : name + " from " + pkg.getName();
	}
}
