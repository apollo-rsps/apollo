package org.apollo.game.io;

import org.apollo.game.plugin.PluginMetaData;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A class that parses {@code plugin.xml} files into {@link PluginMetaData} objects.
 *
 * @author Graham
 */
public final class PluginMetaDataParser {

	/**
	 * An empty xml node array.
	 */
	private static final XmlNode[] EMPTY_NODE_ARRAY = new XmlNode[0];

	/**
	 * The input stream.
	 */
	private final InputStream is;

	/**
	 * The XML parser.
	 */
	private final XmlParser parser;

	/**
	 * Creates the plugin meta data parser.
	 *
	 * @param is The input stream.
	 * @throws SAXException If a SAX error occurs.
	 */
	public PluginMetaDataParser(InputStream is) throws SAXException {
		this.is = is;
		parser = new XmlParser();
	}

	/**
	 * Gets the specified child element, if it exists.
	 *
	 * @param node The root node.
	 * @param name The element name.
	 * @return The node object.
	 * @throws IOException If the element does not exist.
	 */
	private static XmlNode getElement(XmlNode node, String name) throws IOException {
		return Objects.requireNonNull(node.getChild(name), "No " + name + " element found.");
	}

	/**
	 * Parses the XML and creates a meta data object.
	 *
	 * @param base The base path for this plugin
	 * @return The meta data object.
	 * @throws SAXException If a SAX error occurs.
	 * @throws IOException If an I/O error occurs.
	 */
	public PluginMetaData parse(File base) throws IOException, SAXException {
		XmlNode rootNode = parser.parse(is);
		if (!rootNode.getName().equals("plugin")) {
			throw new IOException("Root node must be named plugin.");
		}

		XmlNode idNode = getElement(rootNode, "id");
		XmlNode nameNode = getElement(rootNode, "name");
		XmlNode descriptionNode = getElement(rootNode, "description");
		XmlNode authorsNode = getElement(rootNode, "authors");
		XmlNode scriptsNode = getElement(rootNode, "scripts");
		XmlNode dependenciesNode = getElement(rootNode, "dependencies");
		XmlNode versionNode = getElement(rootNode, "version");

		String id = idNode.getValue();
		String name = nameNode.getValue();
		String description = descriptionNode.getValue();
		double version = Double.parseDouble(versionNode.getValue());

		if (id == null || name == null || description == null) {
			throw new IOException("Id, name and description must have values.");
		}

		XmlNode[] authorNodes = authorsNode.getChildren().toArray(EMPTY_NODE_ARRAY);
		XmlNode[] scriptNodes = scriptsNode.getChildren().toArray(EMPTY_NODE_ARRAY);
		XmlNode[] dependencyNodes = dependenciesNode.getChildren().toArray(EMPTY_NODE_ARRAY);

		String[] authors = new String[authorNodes.length];
		String[] scripts = new String[scriptNodes.length];
		String[] dependencies = new String[dependencyNodes.length];

		for (int i = 0; i < authorNodes.length; i++) {
			authors[i] = authorNodes[i].getValue();
			if (authors[i] == null) {
				throw new IOException("Author elements must have values.");
			}
		}

		for (int i = 0; i < scriptNodes.length; i++) {
			scripts[i] = scriptNodes[i].getValue();
			if (scripts[i] == null) {
				throw new IOException("Script elements must have values.");
			}
		}

		for (int i = 0; i < dependencyNodes.length; i++) {
			dependencies[i] = dependencyNodes[i].getValue();
			if (dependencies[i] == null) {
				throw new IOException("Dependency elements must have values.");
			}
		}

		return new PluginMetaData(id, base, name, description, authors, scripts, dependencies, version);
	}

}