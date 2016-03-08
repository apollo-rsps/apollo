package org.apollo.net;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import com.google.common.base.Preconditions;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * Holds various network-related constants such as port numbers.
 *
 * @author Graham
 * @author Major
 */
public final class NetworkConstants {

	/**
	 * The HTTP port.
	 */
	public static final int HTTP_PORT;

	/**
	 * The number of seconds before a connection becomes idle.
	 */
	public static final int IDLE_TIME = 15;

	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT;

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
	public static final int SERVICE_PORT;

	static {
		try (InputStream is = new FileInputStream("data/net.xml")) {
			XmlNode net = new XmlParser().parse(is);
			if (!net.getName().equals("net")) {
				throw new IOException("Root node name is not 'net'.");
			}

			XmlNode ports = net.getChild("ports");
			Preconditions.checkState(ports != null, "Root node must have a child named 'ports'.");

			XmlNode http = ports.getChild("http"), service = ports.getChild("service"), jaggrab = ports.getChild("jaggrab");
			Preconditions.checkState(http != null && service != null && jaggrab != null, "Ports node must have three children: 'http', 'service', and 'jaggrab'.");

			HTTP_PORT = Integer.parseInt(http.getValue());
			SERVICE_PORT = Integer.parseInt(service.getValue());
			JAGGRAB_PORT = Integer.parseInt(jaggrab.getValue());
		} catch (Exception exception) {
			throw new ExceptionInInitializerError(new IOException("Error parsing net.xml.", exception));
		}

		try (PemReader pemReader = new PemReader(new FileReader("data/rsa.pem"))) {
			PemObject pem = pemReader.readPemObject();
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pem.getContent());

			Security.addProvider(new BouncyCastleProvider());
			KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

			RSAPrivateKey privateKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
			RSA_MODULUS = privateKey.getModulus();
			RSA_EXPONENT = privateKey.getPrivateExponent();
		} catch (Exception exception) {
			throw new ExceptionInInitializerError(new IOException("Error parsing rsa.pem", exception));
		}
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private NetworkConstants() {

	}

}