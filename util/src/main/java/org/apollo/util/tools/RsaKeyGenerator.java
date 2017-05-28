package org.apollo.util.tools;

import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * An RSA key generator.
 *
 * @author Graham
 * @author Major
 * @author Cube
 */
public final class RsaKeyGenerator {

	/**
	 * The bit count.
	 * <strong>Note:</strong> 2048 bits and above are not compatible with the client without modifications
	 */
	private static final int BIT_COUNT = 1024;

	/**
	 * The path to the private key file.
	 */
	private static final String PRIVATE_KEY_FILE = "data/rsa.pem";

	/**
	 * The entry point of the RsaKeyGenerator.
	 *
	 * @param args The application arguments.
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(BIT_COUNT);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		System.out.println("Place these keys in the client:");
		System.out.println("--------------------");
		System.out.println("public key: " + publicKey.getPublicExponent());
		System.out.println("modulus: " + publicKey.getModulus());

		try (PemWriter writer = new PemWriter(new FileWriter(PRIVATE_KEY_FILE))) {
			writer.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
		} catch (Exception e) {
			System.err.println("Failed to write private key to " + PRIVATE_KEY_FILE);
			e.printStackTrace();
		}
	}

}