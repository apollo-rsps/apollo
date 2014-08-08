package org.apollo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * A class which manages {@link Service}s.
 * 
 * @author Graham
 */
public final class ServiceManager {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

	/**
	 * The service map.
	 */
	private Map<Class<? extends Service>, Service> services = new HashMap<>();

	/**
	 * Creates and initializes the {@link ServiceManager}.
	 * 
	 * @throws IOException If there is an error reading from the xml file.
	 * @throws SAXException If there is an error parsing the xml file.
	 * @throws ReflectiveOperationException If there is an error accessing or creating services using reflection.
	 */
	public ServiceManager() throws IOException, SAXException, ReflectiveOperationException {
		init();
	}

	/**
	 * Gets a service.
	 * 
	 * @param <S> The type of service.
	 * @param clazz The service class.
	 * @return The service.
	 */
	@SuppressWarnings("unchecked")
	public <S extends Service> S getService(Class<S> clazz) {
		return (S) services.get(clazz);
	}

	/**
	 * Initializes this service manager.
	 * 
	 * @throws SAXException If the service XML file could not be parsed.
	 * @throws IOException If the file could not be accessed.
	 * @throws IllegalAccessException If the service could not be accessed.
	 * @throws InstantiationException If the service could not be instantiated.
	 * @throws ClassNotFoundException If the service could not be found.
	 */
	@SuppressWarnings("unchecked")
	private void init() throws SAXException, IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		logger.fine("Registering services...");

		XmlParser parser = new XmlParser();
		XmlNode root;

		try (InputStream is = new FileInputStream("data/services.xml")) {
			root = parser.parse(is);
		}

		if (!root.getName().equals("services")) {
			throw new IOException("Unexpected name of root node.");
		}

		for (XmlNode child : root) {
			if (!child.getName().equals("service")) {
				throw new IOException("Unexpected name of child node.");
			}

			if (!child.hasValue()) {
				throw new IOException("Child node must have a value.");
			}

			Class<? extends Service> service = (Class<? extends Service>) Class.forName(child.getValue());
			register((Class<Service>) service, service.newInstance());
		}
	}

	/**
	 * Registers a service.
	 * 
	 * @param <S> The type of service.
	 * @param clazz The service's class.
	 * @param service The service.
	 */
	private <S extends Service> void register(Class<S> clazz, S service) {
		logger.fine("Registering service: " + clazz + "...");
		services.put(clazz, service);
	}

	/**
	 * Sets the context of all services.
	 * 
	 * @param ctx The server context.
	 */
	public void setContext(ServerContext ctx) {
		for (Service service : services.values()) {
			service.setContext(ctx);
		}
	}

	/**
	 * Starts all the services.
	 */
	public void startAll() {
		logger.info("Starting services...");
		for (Service service : services.values()) {
			logger.fine("Starting service: " + service.getClass().getName() + "...");
			service.start();
		}
	}

}