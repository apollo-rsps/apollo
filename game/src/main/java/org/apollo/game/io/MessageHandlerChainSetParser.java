package org.apollo.game.io;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainSet;
import org.apollo.game.model.World;
import org.apollo.net.message.Message;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class that parses the {@code messages.xml} file to produce {@link MessageHandlerChainSet}s.
 *
 * @author Graham
 */
public final class MessageHandlerChainSetParser {

	/**
	 * The source {@link InputStream}.
	 */
	private final InputStream is;

	/**
	 * The {@link XmlParser} instance.
	 */
	private final XmlParser parser = new XmlParser();

	/**
	 * Creates the message chain parser.
	 *
	 * @param is The source {@link InputStream}.
	 * @throws SAXException If a SAX error occurs.
	 */
	public MessageHandlerChainSetParser(InputStream is) throws SAXException {
		this.is = is;
	}

	/**
	 * Parses the XML and produces a group of {@link MessageHandlerChain}s.
	 *
	 * @param world The {@link World} this MessageHandlerChainGroup is for.
	 * @return A {@link MessageHandlerChainSet}.
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If a SAX error occurs.
	 * @throws ReflectiveOperationException If a reflection error occurs.
	 */
	@SuppressWarnings("unchecked")
	public MessageHandlerChainSet parse(World world) throws IOException, SAXException, ReflectiveOperationException {
		XmlNode messages = parser.parse(is);
		if (!messages.getName().equals("messages")) {
			throw new IOException("Root node name is not 'messages'.");
		}

		MessageHandlerChainSet chainSet = new MessageHandlerChainSet();

		for (XmlNode message : messages) {
			if (!message.getName().equals("message")) {
				throw new IOException("Only expected nodes named 'message' beneath the root node.");
			}

			XmlNode typeNode = message.getChild("type");
			if (typeNode == null) {
				throw new IOException("No node named 'type' beneath current message node.");
			}
			XmlNode chainNode = message.getChild("chain");
			if (chainNode == null) {
				throw new IOException("No node named 'chain' beneath current message node.");
			}

			String messageClassName = typeNode.getValue();
			if (messageClassName == null) {
				throw new IOException("Type node must have a value.");
			}

			Class<? extends Message> messageClass = (Class<? extends Message>) Class.forName(messageClassName);

			for (XmlNode handlerNode : chainNode) {
				if (!handlerNode.getName().equals("handler")) {
					throw new IOException("Only expected nodes named 'handler' beneath the root node.");
				}

				String handlerClassName = handlerNode.getValue();
				if (handlerClassName == null) {
					throw new IOException("Handler node must have a value.");
				}

				Class<? extends MessageHandler<? extends Message>> handlerClass = (Class<? extends MessageHandler<? extends Message>>) Class.forName(handlerClassName);
				MessageHandler<? extends Message> handler = handlerClass.getConstructor(World.class).newInstance(world);
				chainSet.putHandler(messageClass, handler);
			}
		}

		return chainSet;
	}

}