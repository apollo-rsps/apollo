package org.apollo.net;

/**
 * Holds various network-related constants such as port numbers.
 * @author Graham
 */
public final class NetworkConstants {

	/**
	 * The service port.
	 */
	public static final int SERVICE_PORT = 43594;

	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT = 43595;

	/**
	 * The HTTP port.
	 */
	public static final int HTTP_PORT = 80;

	/**
	 * The terminator of a string.
	 */
	public static final int STRING_TERMINATOR = 10;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private NetworkConstants() {

	}

}
