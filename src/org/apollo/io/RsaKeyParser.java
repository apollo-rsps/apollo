package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.apollo.net.NetworkConstants;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class that parses the {@code rsa.xml} file.
 * 
 * @author Major
 */
public class RsaKeyParser {

	/**
	 * The source {@link InputStream}.
	 */
	private final InputStream is;

	/**
	 * The {@link XmlParser} instance.
	 */
	private final XmlParser parser;

	/**
	 * Creates the RSA specification parser.
	 * 
	 * @param is The source {@link InputStream}.
	 * @throws SAXException If a SAX error occurs.
	 */
	public RsaKeyParser(InputStream is) throws SAXException {
		parser = new XmlParser();
		this.is = is;
	}

	/**
	 * Parses the {@code rsa.xml} file.
	 * 
	 * @throws SAXException If a SAX error occurs.
	 * @throws IOException
	 */
	public void parse() throws SAXException, IOException {
		XmlNode rootNode = parser.parse(is);
		if (!rootNode.getName().equals("rsa")) {
			throw new IOException("root node name is not 'rsa'");
		}

		XmlNode modulusNode = rootNode.getChild("modulus");
		if (modulusNode == null) {
			throw new IOException("no node named 'modulus' beneath root node");
		}

		XmlNode exponentNode = rootNode.getChild("private-exponent");
		if (exponentNode == null) {
			throw new IOException("no node named 'private-exponent' beneath root node");
		}

		NetworkConstants.RSA_MODULUS = new BigInteger(modulusNode.getValue());
		NetworkConstants.RSA_EXPONENT = new BigInteger(exponentNode.getValue());
	}

}