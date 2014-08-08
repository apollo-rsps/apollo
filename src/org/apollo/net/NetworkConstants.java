package org.apollo.net;

import io.netty.util.AttributeKey;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import org.apollo.net.session.Session;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;

/**
 * Holds various network-related constants such as port numbers.
 * 
 * @author Graham
 */
public final class NetworkConstants {

	/**
	 * The HTTP port.
	 */
	public static final int HTTP_PORT = 80;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT = 43595;

	/**
	 * The exponent used when decrypting the RSA block.
	 */
	public static final BigInteger RSA_EXPONENT;

	/**
	 * The modulus used when decrypting the RSA block.
	 */
	public static final BigInteger RSA_MODULUS;

	/**
	 * The service port.
	 */
	public static final int SERVICE_PORT = 43594;

	/**
	 * The {@link Session} {@link AttributeKey}.
	 */
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

	/**
	 * The terminator of a string.
	 */
	public static final int STRING_TERMINATOR = 10;

	static {
		try (InputStream is = new FileInputStream("data/rsa.xml")) {
			XmlNode rsa = new XmlParser().parse(is);
			if (!rsa.getName().equals("rsa")) {
				throw new IOException("Root node name is not 'rsa'.");
			}

			XmlNode modulus = rsa.getChild("modulus"), exponent = rsa.getChild("private-exponent");
			if (modulus == null || exponent == null) {
				throw new IOException("Root node must have two children - 'modulus' and 'private-exponent'.");
			}

			RSA_MODULUS = new BigInteger(modulus.getValue());
			RSA_EXPONENT = new BigInteger(exponent.getValue());
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private NetworkConstants() {

	}

}