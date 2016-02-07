package org.apollo.game.model.entity.attr;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * An {@link Attribute} with a string value.
 *
 * @author Major
 */
public final class StringAttribute extends Attribute<String> {

	/**
	 * Creates the string attribute.
	 *
	 * @param value The value.
	 */
	public StringAttribute(String value) {
		this(value, false);
	}

	/**
	 * Creates the string attribute.
	 *
	 * @param value The value.
	 * @param symbol Whether or not the attribute is a symbol.
	 */
	public StringAttribute(String value, boolean symbol) {
		super(symbol ? AttributeType.SYMBOL : AttributeType.STRING, value);
	}

	@Override
	public byte[] encode() {
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		int length = bytes.length;

		bytes = Arrays.copyOf(bytes, length + 1);
		bytes[length] = 0;
		return bytes;
	}

	@Override
	public String toString() {
		return value;
	}

}