package org.apollo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.game.model.World;
import org.apollo.net.ApolloHandler;
import org.apollo.net.HttpChannelInitializer;
import org.apollo.net.JagGrabChannelInitializer;
import org.apollo.net.NetworkConstants;
import org.apollo.net.ServiceChannelInitializer;
import org.apollo.net.release.Release;
import org.apollo.net.release.r317.Release317;
import org.apollo.util.plugin.PluginContext;
import org.apollo.util.plugin.PluginManager;

/**
 * The core class of the Apollo server.
 * 
 * @author Graham
 */
public final class Server {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The entry point of the Apollo server application.
	 * 
	 * @param args The command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			Server server = new Server();
			server.init(args.length == 1 ? args[0] : Release317.class.getName());

			SocketAddress service = new InetSocketAddress(NetworkConstants.SERVICE_PORT);
			SocketAddress http = new InetSocketAddress(NetworkConstants.HTTP_PORT);
			SocketAddress jaggrab = new InetSocketAddress(NetworkConstants.JAGGRAB_PORT);

			server.start();
			server.bind(service, http, jaggrab);
			logger.fine("Starting apollo took " + (System.currentTimeMillis() - start) + " ms.");
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error whilst starting server.", t);
		}
	}

	/**
	 * The server's context.
	 */
	private ServerContext context;

	/**
	 * The {@link ServerBootstrap} for the HTTP listener.
	 */
	private final ServerBootstrap httpBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the JAGGRAB listener.
	 */
	private final ServerBootstrap jagGrabBootstrap = new ServerBootstrap();

	/**
	 * The {@link ServerBootstrap} for the service listener.
	 */
	private final ServerBootstrap serviceBootstrap = new ServerBootstrap();

	/**
	 * The event loop group.
	 */
	private final EventLoopGroup loopGroup = new NioEventLoopGroup();

	/**
	 * The service manager.
	 */
	private final ServiceManager serviceManager = new ServiceManager();

	/**
	 * Creates the Apollo server.
	 * 
	 * @throws Exception If an error occurs whilst creating services.
	 */
	public Server() throws Exception {
		logger.info("Starting Apollo...");
	}

	/**
	 * Binds the server to the specified address.
	 * 
	 * @param serviceAddress The service address to bind to.
	 * @param httpAddress The HTTP address to bind to.
	 * @param jagGrabAddress The JAGGRAB address to bind to.
	 */
	public void bind(SocketAddress serviceAddress, SocketAddress httpAddress, SocketAddress jagGrabAddress) {
		logger.fine("Binding service listener to address: " + serviceAddress + "...");
		serviceBootstrap.bind(serviceAddress);

		logger.fine("Binding HTTP listener to address: " + httpAddress + "...");
		try {
			httpBootstrap.bind(httpAddress);
		} catch (Throwable t) {
			logger.log(Level.WARNING,
					"Binding to HTTP failed: client will use JAGGRAB as a fallback (not recommended)!", t);
		}

		logger.fine("Binding JAGGRAB listener to address: " + jagGrabAddress + "...");
		jagGrabBootstrap.bind(jagGrabAddress);

		logger.info("Ready for connections.");
	}

	/**
	 * Initialises the server.
	 * 
	 * @param releaseClassName The class name of the current active {@link Release}.
	 * @throws ClassNotFoundException If the release class could not be found.
	 * @throws IllegalAccessException If the release class could not be accessed.
	 * @throws InstantiationException If the release class could not be instantiated.
	 */
	public void init(String releaseClassName) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		Class<?> clazz = Class.forName(releaseClassName);
		Release release = (Release) clazz.newInstance();

		logger.info("Initialized release #" + release.getReleaseNumber() + ".");

		serviceBootstrap.group(loopGroup);
		httpBootstrap.group(loopGroup);
		jagGrabBootstrap.group(loopGroup);

		context = new ServerContext(release, serviceManager);
		ApolloHandler handler = new ApolloHandler(context);

		ChannelInitializer<SocketChannel> serviceInitializer = new ServiceChannelInitializer(handler);
		serviceBootstrap.channel(NioServerSocketChannel.class);
		serviceBootstrap.childHandler(serviceInitializer);

		ChannelInitializer<SocketChannel> httpInitializer = new HttpChannelInitializer(handler);
		httpBootstrap.channel(NioServerSocketChannel.class);
		httpBootstrap.childHandler(httpInitializer);

		ChannelInitializer<SocketChannel> jagGrabInitializer = new JagGrabChannelInitializer(handler);
		jagGrabBootstrap.channel(NioServerSocketChannel.class);
		jagGrabBootstrap.childHandler(jagGrabInitializer);
	}

	/**
	 * Starts the server.
	 * 
	 * @throws Exception If an error occurs.
	 */
	public void start() throws Exception {
		PluginManager manager = new PluginManager(new PluginContext(context));
		serviceManager.startAll();

		int releaseNo = context.getRelease().getReleaseNumber();
		IndexedFileSystem fs = new IndexedFileSystem(Paths.get("data/fs/", Integer.toString(releaseNo)), true);
		World.getWorld().init(releaseNo, fs, manager);
	}

}