package org.apollo.net;

import java.math.BigInteger;

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
     * If or not the login block uses RSA to encrypt the credentials
     */
    public static boolean LOGIN_BLOCK_USES_RSA = false;

    /**
     * The exponent used when decrypting the RSA block.
     */
    public static BigInteger RSA_EXPONENT;

    /**
     * The modulus used when decrypting the RSA block.
     */
    public static BigInteger RSA_MODULUS;

    /**
     * The service port.
     */
    public static final int SERVICE_PORT = 43594;

    /**
     * The terminator of a string.
     */
    public static final int STRING_TERMINATOR = 10;

    /**
     * Default private constructor to prevent instantiation by other classes.
     */
    private NetworkConstants() {

    }

}