package org.apollo.tools;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * An RSA key generator.
 *
 * @author Graham
 * @author Major
 */
public final class RsaKeyGenerator {

	/**
	 * The bit count. <strong>Strongly</strong> recommended to be at least 2,048.
	 */
	private static final int BIT_COUNT = 2_048;

	/**
	 * The entry point of the RsaKeyGenerator.
	 * 
	 * @param args The application arguments.
	 */
	public static void main(String[] args) {
		Random random = new SecureRandom();

		BigInteger publicKey = BigInteger.valueOf(65_537);
		BigInteger p, q, phi, modulus, privateKey;

		do {
			p = BigInteger.probablePrime(BIT_COUNT / 2, random);
			q = BigInteger.probablePrime(BIT_COUNT / 2, random);
			phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

			modulus = p.multiply(q);
			privateKey = publicKey.modInverse(phi);
		} while (modulus.bitLength() != BIT_COUNT || privateKey.bitLength() != BIT_COUNT
				|| !phi.gcd(publicKey).equals(BigInteger.ONE));

		System.out.println("modulus: " + modulus);
		System.out.println("public key: " + publicKey);
		System.out.println("private key: " + privateKey);
	}

}