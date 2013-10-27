package org.apollo;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.game.model.World;
import org.apollo.net.ApolloHandler;
import org.apollo.net.HttpPipelineFactory;
import org.apollo.net.JagGrabPipelineFactory;
import org.apollo.net.ServicePipelineFactory;
import org.apollo.net.NetworkConstants;
import org.apollo.net.release.Release;
import org.apollo.net.release.r317.Release317;
import org.apollo.util.plugin.PluginContext;
import org.apollo.util.plugin.PluginManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

/**
 * The core class of the Apollo server.
 * @author Graham
 */
public final class Server {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The entry point of the Apollo server application.
	 * @param args The command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		Server server = null;
		try {
			server = new Server();
			server.init(args.length == 1 ? args[0] : Release317.class.getName());

			SocketAddress service = new InetSocketAddress(NetworkConstants.SERVICE_PORT);
			SocketAddress http = new InetSocketAddress(NetworkConstants.HTTP_PORT);
			SocketAddress jaggrab = new InetSocketAddress(NetworkConstants.JAGGRAB_PORT);

			server.start();
			server.bind(service, http, jaggrab);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error whilst starting server.", t);
		}
	}

	/**
	 * The {@link ServerBootstrap} for the service listener.
	 */
	private final ServerBootstrap serviceBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the HTTP listener.
	 */
	private final ServerBootstrap httpBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the JAGGRAB listener.
	 */
	private final ServerBootstrap jagGrabBootstrap = new ServerBootstrap();

	/**
	 * The {@link ExecutorService} used for network events. The named thread
	 * factory is unused as Netty names threads itself.
	 */
	private final ExecutorService networkExecutor = Executors.newCachedThreadPool();

	/**
	 * The service manager.
	 */
	private final ServiceManager serviceManager;

	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer = new HashedWheelTimer();

	/**
	 * The server's context.
	 */
	private ServerContext context;

	/**
	 * Creates the Apollo server.
	 * @throws Exception if an error occurs whilst creating services.
	 */
	public Server() throws Exception {
		logger.info("Starting Apollo...");
		serviceManager = new ServiceManager();
	}

	/**
	 * Initialises the server.
	 * @param releaseClassName The class name of the current active
	 * {@link Release}.
	 * @throws ClassNotFoundException if the release class could not be found.
	 * @throws IllegalAccessException if the release class could not be accessed.
	 * @throws InstantiationException if the release class could not be instantiated.
	 */
	public void init(String releaseClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> clazz = Class.forName(releaseClassName);
		Release release = (Release) clazz.newInstance();

		logger.info("Initialized release #" + release.getReleaseNumber() + ".");

		ChannelFactory factory = new NioServerSocketChannelFactory(networkExecutor, networkExecutor);
		serviceBootstrap.setFactory(factory);
		httpBootstrap.setFactory(factory);
		jagGrabBootstrap.setFactory(factory);

		context = new ServerContext(release, serviceManager);
		ApolloHandler handler = new ApolloHandler(context);

		ChannelPipelineFactory servicePipelineFactory = new ServicePipelineFactory(handler, timer);
		serviceBootstrap.setPipelineFactory(servicePipelineFactory);

		ChannelPipelineFactory httpPipelineFactory = new HttpPipelineFactory(handler, timer);
		httpBootstrap.setPipelineFactory(httpPipelineFactory);

		ChannelPipelineFactory jagGrabPipelineFactory = new JagGrabPipelineFactory(handler, timer);
		jagGrabBootstrap.setPipelineFactory(jagGrabPipelineFactory);
	}

	/**
	 * Binds the server to the specified address.
	 * @param serviceAddress The service address to bind to.
	 * @param httpAddress The HTTP address to bind to.
	 * @param jagGrabAddress The JAGGRAB address to bind to.
	 */
	public void bind(SocketAddress serviceAddress, SocketAddress httpAddress, SocketAddress jagGrabAddress) {
		logger.info("Binding service listener to address: " + serviceAddress + "...");
		serviceBootstrap.bind(serviceAddress);

		logger.info("Binding HTTP listener to address: " + httpAddress + "...");
		try {
			httpBootstrap.bind(httpAddress);
		} catch (Throwable t) {
			logger.log(Level.WARNING, "Binding to HTTP failed: client will use JAGGRAB as a fallback (not reccomended)!", t);
		}

		logger.info("Binding JAGGRAB listener to address: " + jagGrabAddress + "...");
		jagGrabBootstrap.bind(jagGrabAddress);

		logger.info("Ready for connections.");
	}

	/**
	 * Starts the server.
	 * @throws Exception if an error occurs.
	 */
	public void start() throws Exception {
		PluginManager mgr = new PluginManager(new PluginContext(context));
		mgr.start(); // TODO move this?

		serviceManager.startAll();

		// TODO move this?
		int releaseNo = context.getRelease().getReleaseNumber();
		IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/" + releaseNo), true);
		World.getWorld().init(releaseNo, fs, mgr);
	}

}
