package org.apollo.net;

import io.netty.util.AttributeKey;

import java.math.BigInteger;

import org.apollo.net.session.Session;

/**
 * Holds various network-related constants such as port numbers.
 * 
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
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The terminator of a string.
	 */
	public static final int STRING_TERMINATOR = 10;

	/**
	 * The exponent used when decrypting the RSA block.
	 */
	public static BigInteger RSA_EXPONENT;

	/**
	 * The modulus used when decrypting the RSA block.
	 */
	public static BigInteger RSA_MODULUS;

	/**
	 * The {@link Session} {@link AttributeKey}.
	 */
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private NetworkConstants() {

	}

}