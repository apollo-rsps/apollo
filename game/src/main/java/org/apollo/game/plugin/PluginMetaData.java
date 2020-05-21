package org.apollo.game.plugin;

import java.io.File;

/**
 * Contains attributes which describe a plugin.
 *
 * @author Graham
 */
public final class PluginMetaData {

	/**
	 * The plugin's authors.
	 */
	private final String[] authors;

	/**
	 * The plugin's dependencies.
	 */
	private final String[] dependencies;

	/**
	 * The plugin's description.
	 */
	private final String description;

	/**
	 * The plugin's id.
	 */
	private final String id;

	/**
	 * The plugin's name.
	 */
	private final String name;

	/**
	 * The plugin's scripts.
	 */
	private final String[] scripts;

	/**
	 * The plugin's version.
	 */
	private final double version;

	/**
	 * The plugin's base folder
	 */
	private final File base;

	/**
	 * Creates the plugin meta data.
	 *
	 * @param id The plugin's id.
	 * @param base The plugin's base folder.
	 * @param name The plugin's name.
	 * @param description The plugin's description.
	 * @param authors The plugin's authors.
	 * @param scripts The plugin's scripts.
	 * @param dependencies The plugin's dependencies.
	 * @param version The plugin's version.
	 */
	public PluginMetaData(String id, File base, String name, String description, String[] authors, String[] scripts, String[] dependencies, double version) {
		this.id = id;
		this.base = base;
		this.name = name;
		this.description = description;
		this.authors = authors;
		this.scripts = scripts;
		this.dependencies = dependencies;
		this.version = version;
	}

	/**
	 * Gets the plugin's authors.
	 *
	 * @return The plugin's authors.
	 */
	public String[] getAuthors() {
		return authors;
	}

	/**
	 * Gets the plugin's dependencies.
	 *
	 * @return The plugin's dependencies.
	 */
	public String[] getDependencies() {
		return dependencies;
	}

	/**
	 * Gets the plugin's description.
	 *
	 * @return The plugin's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the plugin's id.
	 *
	 * @return The plugin's id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the plugin's name.
	 *
	 * @return The plugin's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the plugin's scripts.
	 *
	 * @return The plugin's scripts.
	 */
	public String[] getScripts() {
		return scripts;
	}

	/**
	 * Gets the plugin's version.
	 *
	 * @return The plugin's version.
	 */
	public double getVersion() {
		return version;
	}

	/**
	 * Gets the plugin's base folder.
	 *
	 * @return The plugin's base folder (where plugin.xml is)
	 */
	public File getBase() {
		return base;
	}
}