package org.apollo.game.model.entity.attr;

/**
 * An {@link Attribute} with a boolean value.
 *
 * @author Major
 */
public final class BooleanAttribute extends Attribute<Boolean> {

	/**
	 * Creates the boolean attribute.
	 *
	 * @param value The value.
	 */
	public BooleanAttribute(Boolean value) {
		super(AttributeType.BOOLEAN, value);
	}

	@Override
	public byte[] encode() {
		return new byte[] { (byte) (value ? 1 : 0) };
	}

	@Override
	public String toString() {
		return Boolean.toString(value);
	}

}