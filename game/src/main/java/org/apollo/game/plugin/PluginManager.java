package org.apollo.game.plugin;

import org.apollo.game.io.PluginMetaDataParser;
import org.apollo.game.model.World;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class which manages plugins.
 *
 * @author Graham
 */
public final class PluginManager {

	/**
	 * A set of all author names.
	 */
	private final SortedSet<String> authors = new TreeSet<>();

	/**
	 * The plugin context.
	 */
	private final PluginContext context;

	/**
	 * The World this PluginManager is for.
	 */
	private final World world;

	/**
	 * Creates the PluginManager.
	 *
	 * @param world The {@link World} the PluginManager is for.
	 * @param context The PluginContext.
	 */
	public PluginManager(World world, PluginContext context) {
		this.context = context;
		this.world = world;
		initAuthors();
	}

	/**
	 * Creates a plugin map from a collection.
	 *
	 * @param plugins The plugin collection.
	 * @return The plugin map.
	 */
	private static Map<String, PluginMetaData> createMap(Collection<PluginMetaData> plugins) {
		Map<String, PluginMetaData> map = plugins.stream().collect(Collectors.toMap(PluginMetaData::getId, Function.identity()));
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
		return findPlugins(new File("./game/data/plugins"));
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
		//@todo - load metadata and respective plugins
		Map<String, PluginMetaData> plugins = new HashMap<>();

		PluginEnvironment env = new KotlinPluginEnvironment(world); // TODO isolate plugins if possible in the future!
		env.setContext(context);

		env.load(plugins.values());
	}

}