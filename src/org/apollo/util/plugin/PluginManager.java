package org.apollo.util.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apollo.io.PluginMetaDataParser;
import org.xml.sax.SAXException;

/**
 * A class which manages plugins.
 * 
 * @author Graham
 */
public final class PluginManager {

	/**
	 * A set of all author names.
	 */
	private SortedSet<String> authors = new TreeSet<>();

	/**
	 * The plugin context.
	 */
	private final PluginContext context;

	/**
	 * Creates the plugin manager.
	 * 
	 * @param context The plugin context.
	 */
	public PluginManager(PluginContext context) {
		this.context = context;
		initAuthors();
	}

	/**
	 * Creates a plugin map from a collection.
	 * 
	 * @param plugins The plugin collection.
	 * @return The plugin map.
	 */
	private Map<String, PluginMetaData> createMap(Collection<PluginMetaData> plugins) {
		Map<String, PluginMetaData> map = new HashMap<>();
		for (PluginMetaData plugin : plugins) {
			map.put(plugin.getId(), plugin);
		}
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Finds plugins and loads their meta data.
	 * 
	 * @return A collection of plugin meta data objects.
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If a SAX error occurs.
	 */
	private Collection<PluginMetaData> findPlugins() throws IOException, SAXException {
		return findPlugins(new File("./data/plugins"));
	}

	/**
	 * Finds plugins and loads their meta data.
	 * 
	 * @param dir The directory to search
	 * 
	 * @return A collection of plugin meta data objects.
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If a SAX error occurs.
	 */
	private Collection<PluginMetaData> findPlugins(File dir) throws IOException, SAXException {
		Collection<PluginMetaData> plugins = new ArrayList<>();
		for (File plugin : dir.listFiles()) {
			if (plugin.isDirectory() && !plugin.getName().startsWith(".")) {
				File xml = new File(plugin, "plugin.xml");
				if (xml.exists()) {
					try (InputStream is = new FileInputStream(xml)) {
						PluginMetaDataParser parser = new PluginMetaDataParser(is);
						PluginMetaData meta = parser.parse(plugin);
						for (String author : meta.getAuthors()) {
							authors.add(author);
						}
						plugins.add(meta);
					}
				} else {
					plugins.addAll(findPlugins(plugin));
				}
			}
		}
		return Collections.unmodifiableCollection(plugins);
	}

	/**
	 * Creates an unmodifiable {@link Set} containing the authors.
	 * 
	 * @return The set.
	 */
	public Set<String> getAuthors() {
		return Collections.unmodifiableSortedSet(authors);
	}

	/**
	 * Populates the list with the authors of the Apollo core.
	 */
	private void initAuthors() {
		// Please don't remove or add names here, unless that person worked on
		// the Apollo core! See the notice in CreditsCommandListener for more
		// information about why we think you shouldn't (tl;dr we put loads of
		// effort into it for free, keeping the credits intact is the least you
		// can do in return).
		//
		// If you want to add your own name, make a plugin and add your name
		// to the plugin.xml file.
		//
		// Thank you!
		authors.add("Graham");
		authors.add("Major");
		authors.add("Chris Fletcher");
		authors.add("Blake");
	}

	/**
	 * Starts the plugin system by finding and loading all the plugins.
	 * 
	 * @throws SAXException If a SAX error occurs.
	 * @throws IOException If an I/O error occurs.
	 * @throws DependencyException If a dependency could not be resolved.
	 */
	public void start() throws IOException, SAXException, DependencyException {
		Map<String, PluginMetaData> plugins = createMap(findPlugins());
		Set<PluginMetaData> started = new HashSet<>();

		PluginEnvironment env = new RubyPluginEnvironment(); // TODO isolate plugins if possible in the future!
		env.setContext(context);

		for (PluginMetaData plugin : plugins.values()) {
			start(env, plugin, plugins, started);
		}
	}

	/**
	 * Starts a specific plugin.
	 * 
	 * @param env The environment.
	 * @param plugin The plugin.
	 * @param plugins The plugin map.
	 * @param started A set of started plugins.
	 * @throws DependencyException If a dependency error occurs.
	 * @throws IOException If an I/O error occurs.
	 */
	private void start(PluginEnvironment env, PluginMetaData plugin, Map<String, PluginMetaData> plugins,
			Set<PluginMetaData> started) throws DependencyException, IOException {
		// TODO check for cyclic dependencies! this way just won't cut it, we need an exception
		if (started.contains(plugin)) {
			return;
		}
		started.add(plugin);

		for (String dependencyId : plugin.getDependencies()) {
			PluginMetaData dependency = plugins.get(dependencyId);
			if (dependency == null) {
				throw new DependencyException("Unresolved dependency: " + dependencyId + ".");
			}
			start(env, dependency, plugins, started);
		}

		String[] scripts = plugin.getScripts();

		for (String script : scripts) {
			File scriptFile = new File(plugin.getBase(), script);
			InputStream is = new FileInputStream(scriptFile);
			env.parse(is, scriptFile.getAbsolutePath());
		}
	}

}