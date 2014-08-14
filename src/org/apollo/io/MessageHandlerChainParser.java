package org.apollo.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apollo.game.message.Message;
import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerChain;
import org.apollo.game.message.handler.MessageHandlerChainGroup;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class that parses the {@code messages.xml} file to produce {@link MessageHandlerChainGroup}s.
 * 
 * @author Graham
 */
public final class MessageHandlerChainParser {

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
	public MessageHandlerChainParser(InputStream is) throws SAXException {
		this.is = is;
	}

	/**
	 * Parses the XML and produces a group of {@link MessageHandlerChain}s.
	 * 
	 * @throws IOException If an I/O error occurs.
	 * @throws SAXException If a SAX error occurs.
	 * @throws ClassNotFoundException If a class was not found.
	 * @throws IllegalAccessException If a class was accessed illegally.
	 * @throws InstantiationException If a class could not be instantiated.
	 * @return A {@link MessageHandlerChainGroup}.
	 */
	@SuppressWarnings("unchecked")
	public MessageHandlerChainGroup parse() throws IOException, SAXException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		XmlNode messages = parser.parse(is);
		if (!messages.getName().equals("messages")) {
			throw new IOException("Root node name is not 'messages'.");
		}

		Map<Class<? extends Message>, MessageHandlerChain<?>> chains = new HashMap<>();

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
			List<MessageHandler<?>> handlers = new ArrayList<>();

			for (XmlNode handlerNode : chainNode) {
				if (!handlerNode.getName().equals("handler")) {
					throw new IOException("Only expected nodes named 'handler' beneath the root node.");
				}

				String handlerClassName = handlerNode.getValue();
				if (handlerClassName == null) {
					throw new IOException("Handler node must have a value.");
				}

				Class<? extends MessageHandler<?>> handlerClass = (Class<? extends MessageHandler<?>>) Class
						.forName(handlerClassName);
				MessageHandler<?> handler = handlerClass.newInstance();
				handlers.add(handler);
			}

			MessageHandler<?>[] handlersArray = handlers.toArray(new MessageHandler<?>[handlers.size()]);
			@SuppressWarnings("rawtypes")
			MessageHandlerChain<?> chain = new MessageHandlerChain(handlersArray);

			chains.put(messageClass, chain);
		}

		return new MessageHandlerChainGroup(chains);
	}

}