package org.apollo.net.codec.game;

/**
 * Represents the order of bytes in a {@link DataType} when
 * {@code {@link DataType#getBytes()} > 1}.
 * @author Graham
 */
public enum DataOrder {

	/**
	 * Least significant byte to most significant byte.
	 */
	LITTLE,

	/**
	 * Also known as the V1 order.
	 */
	MIDDLE,

	/**
	 * Also known as the V2 order.
	 */
	INVERSED_MIDDLE,

	/**
	 * Most significant byte to least significant byte.
	 */
	BIG;

}
