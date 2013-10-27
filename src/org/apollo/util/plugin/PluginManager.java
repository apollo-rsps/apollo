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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apollo.io.PluginMetaDataParser;
import org.xml.sax.SAXException;

/**
 * A class which manages plugins.
 * @author Graham
 */
public final class PluginManager {

	/**
	 * The plugin context.
	 */
	private final PluginContext context;

	/**
	 * A set of all author names.
	 */
	private SortedSet<String> authors = new TreeSet<String>();

	/**
	 * Creates the plugin manager.
	 * @param context The plugin context.
	 */
	public PluginManager(PluginContext context) {
		this.context = context;
		initAuthors();
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
		authors.add("Blake");
	}

	/**
	 * Creates an iterator for the set of authors.
	 * @return The iterator.
	 */
	public Iterator<String> createAuthorsIterator() {
		return authors.iterator();
	}

	/**
	 * Starts the plugin system by finding and loading all the plugins.
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 * @throws DependencyException if a dependency could not be resolved.
	 */
	public void start() throws IOException, SAXException, DependencyException {
		Map<String, PluginMetaData> plugins = createMap(findPlugins());
		Set<PluginMetaData> started = new HashSet<PluginMetaData>();

		PluginEnvironment env = new RubyPluginEnvironment(); // TODO isolate plugins if possible in the future!
		env.setContext(context);

		for (PluginMetaData plugin : plugins.values()) {
			start(env, plugin, plugins, started);
		}
	}

	/**
	 * Finds plugins and loads their meta data.
	 * @return A collection of plugin meta data objects.
	 * @throws IOException if an I/O error occurs.
	 * @throws SAXException if a SAX error occurs.
	 */
	private Collection<PluginMetaData> findPlugins() throws IOException, SAXException {
		Collection<PluginMetaData> plugins = new ArrayList<PluginMetaData>();
		File dir = new File("./data/plugins");
		for (File plugin : dir.listFiles()) {
			if (plugin.isDirectory() && !plugin.getName().startsWith(".")) {
				File xml = new File(plugin, "plugin.xml");
				if (xml.exists()) {
					InputStream is = new FileInputStream(xml);
					try {
						PluginMetaDataParser parser = new PluginMetaDataParser(is);
						PluginMetaData meta = parser.parse();
						for (String author : meta.getAuthors()) {
							authors.add(author);
						}
						plugins.add(meta);
					} finally {
						is.close();
					}
				}
			}
		}
		return Collections.unmodifiableCollection(plugins);
	}

	/**
	 * Starts a specific plugin.
	 * @param env The environment.
	 * @param plugin The plugin.
	 * @param plugins The plugin map.
	 * @param started A set of started plugins.
	 * @throws DependencyException if a dependency error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	private void start(PluginEnvironment env, PluginMetaData plugin, Map<String, PluginMetaData> plugins, Set<PluginMetaData> started) throws DependencyException, IOException {
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

			start(env, plugin, plugins, started);
		}

		String[] scripts = plugin.getScripts();

		for (String script : scripts) {
			File f = new File("./data/plugins/" + plugin.getId() + "/" + script); // TODO get from metadata obj?
			InputStream is = new FileInputStream(f);
			env.parse(is, f.getAbsolutePath());
		}
	}

	/**
	 * Creates a plugin map from a collection.
	 * @param plugins The plugin collection.
	 * @return The plugin map.
	 */
	private Map<String, PluginMetaData> createMap(Collection<PluginMetaData> plugins) {
		Map<String, PluginMetaData> map = new HashMap<String, PluginMetaData>();
		for (PluginMetaData plugin : plugins) {
			map.put(plugin.getId(), plugin);
		}
		return Collections.unmodifiableMap(map);
	}

}
