package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type If set angle message.
 *
 * @author Khaled Abdeljaber
 */
public class IfSetColourMessage extends Message {

	/**
	 * The packed interface.
	 */
	private final int packedInterface;

	/**
	 * The packed colors.
	 */
	private final int packedColours;

	/**
	 * Instantiates a new If set colour message.
	 *
	 * @param interfaceId   the interface id
	 * @param componentId   the component id
	 * @param packedColours the packed colours
	 */
	public IfSetColourMessage(int interfaceId, int componentId, int packedColours) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.packedColours = packedColours;
	}

	/**
	 * Instantiates a new If set colour message.
	 *
	 * @param interfaceId the interface id
	 * @param componentId the component id
	 * @param red         the red
	 * @param green       the green
	 * @param blue        the blue
	 */
	public IfSetColourMessage(int interfaceId, int componentId, int red, int green, int blue) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.packedColours = red << 10 | green << 5 | blue;
	}

	/**
	 * Gets packed interface.
	 *
	 * @return the packed interface
	 */
	public int getPackedInterface() {
		return packedInterface;
	}

	/**
	 * Gets packed colours.
	 *
	 * @return the packed colours
	 */
	public int getPackedColours() {
		return packedColours;
	}
}
