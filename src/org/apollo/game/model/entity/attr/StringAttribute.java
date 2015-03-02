package org.apollo.game.model.entity.attr;

import java.nio.charset.Charset;

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
		return value.getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public String toString() {
		return value;
	}

}