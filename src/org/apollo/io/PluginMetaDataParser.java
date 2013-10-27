package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;

import org.apollo.util.plugin.PluginMetaData;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which parses {@code plugin.xml} files into {@link PluginMetaData}
 * objects.
 * @author Graham
 */
public final class PluginMetaDataParser {

	/**
	 * An empty xml node array.
	 */
	private static final XmlNode[] EMPTY_NODE_ARRAY = new XmlNode[0];

	/**
	 * The XML parser.
	 */
	private final XmlParser parser;

	/**
	 * The input stream.
	 */
	private final InputStream is;

	/**
	 * Creates the plugin meta data parser.
	 * @param is The input stream.
	 * @throws SAXException if a SAX error occurs.
	 */
	public PluginMetaDataParser(InputStream is) throws SAXException {
		this.parser = new XmlParser();
		this.is = is;
	}

	/**
	 * Parses the XML and creates a meta data object.
	 * @return The meta data object.
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	public PluginMetaData parse() throws IOException, SAXException {
		XmlNode rootNode = parser.parse(is);
		if (!rootNode.getName().equals("plugin")) {
			throw new IOException("root node must be named plugin");
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
		int version = Integer.parseInt(versionNode.getValue());

		if (id == null || name == null || description == null) {
			throw new IOException("id, name and description must have values");
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
				throw new IOException("author elements must have values");
			}
		}

		for (int i = 0; i < scriptNodes.length; i++) {
			scripts[i] = scriptNodes[i].getValue();
			if (scripts[i] == null) {
				throw new IOException("script elements must have values");
			}
		}

		for (int i = 0; i < dependencyNodes.length; i++) {
			dependencies[i] = dependencyNodes[i].getValue();
			if (dependencies[i] == null) {
				throw new IOException("dependency elements must have values");
			}
		}

		return new PluginMetaData(id, name, description, authors, scripts, dependencies, version);
	}

	/**
	 * Gets the specified child element, if it exists.
	 * @param node The root node.
	 * @param name The element name.
	 * @return The node object.
	 * @throws IOException if the element does not exist.
	 */
	private XmlNode getElement(XmlNode node, String name) throws IOException {
		XmlNode child = node.getChild(name);
		if (child == null) {
			throw new IOException("no " + name + " element found");
		}
		return child;
	}

}
