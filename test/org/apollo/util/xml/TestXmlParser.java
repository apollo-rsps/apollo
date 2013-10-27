package org.apollo.util.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * A test for the {@link XmlParser} class.
 * @author Graham
 */
public final class TestXmlParser {

	/**
	 * A test for the {@link XmlParser#parse(java.io.InputStream)} method.
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testParseInputStream() throws SAXException, IOException {
		XmlParser parser = new XmlParser();
		InputStream is = new ByteArrayInputStream("<root a='1' b='2' c='3'><z><y><x></x></y></z></root>".getBytes());
		XmlNode root = parser.parse(is);

		assertEquals(root.getName(), "root");
		assertEquals(root.getAttributeCount(), 3);
		assertEquals(root.getChildCount(), 1);
		assertFalse(root.hasValue());

		Set<String> attributeNames = root.getAttributeNames();
		assertTrue(attributeNames.contains("a"));
		assertTrue(attributeNames.contains("b"));
		assertTrue(attributeNames.contains("c"));
		assertFalse(attributeNames.contains("z"));
		assertFalse(attributeNames.contains("y"));
		assertFalse(attributeNames.contains("x"));

		assertEquals("1", root.getAttribute("a"));
		assertEquals("2", root.getAttribute("b"));
		assertEquals("3", root.getAttribute("c"));
		assertNull(root.getAttribute("z"));
		assertNull(root.getAttribute("y"));
		assertNull(root.getAttribute("x"));

		XmlNode[] firstChild = root.getChildren().toArray(new XmlNode[1]);
		assertEquals(1, firstChild.length);
		assertEquals("z", firstChild[0].getName());

		XmlNode[] secondChild = firstChild[0].getChildren().toArray(new XmlNode[1]);
		assertEquals(1, secondChild.length);
		assertEquals("y", secondChild[0].getName());

		XmlNode[] thirdChild = secondChild[0].getChildren().toArray(new XmlNode[1]);
		assertEquals(1, thirdChild.length);
		assertEquals("x", thirdChild[0].getName());

		assertEquals(0, thirdChild[0].getChildCount());
	}

	/**
	 * A test for the {@link XmlParser#parse(java.io.Reader)} method.
	 * @throws SAXException if a SAX error occurs.
	 * @throws IOException if an I/O error occurs.
	 */
	@Test
	public void testParseReader() throws SAXException, IOException {
		XmlParser parser = new XmlParser();
		Reader reader = new StringReader("<alphabet><a>1</a><b>2</b><c>3</c></alphabet>");
		XmlNode root = parser.parse(reader);

		assertEquals(root.getName(), "alphabet");
		assertEquals(root.getAttributeCount(), 0);
		assertEquals(root.getChildCount(), 3);
		assertFalse(root.hasValue());

		XmlNode[] children = root.getChildren().toArray(new XmlNode[3]);

		assertEquals(children[0].getName(), "a");
		assertEquals(children[1].getName(), "b");
		assertEquals(children[2].getName(), "c");

		assertEquals(children[0].getValue(), "1");
		assertEquals(children[1].getValue(), "2");
		assertEquals(children[2].getValue(), "3");

		for (int i = 0; i < 3; i++) {
			assertTrue(children[i].hasValue());
			assertEquals(children[i].getAttributeCount(), 0);
		}
	}

}
