package org.apollo.game.model.entity.attr;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.primitives.Longs;

/**
 * Tests for different {@link Attribute}s.
 */
public final class AttributeTests {

	/**
	 * Defines Attributes with their default values before any tests are run.
	 */
	@Before
	public void setup() {
		AttributeMap.define("string_test", AttributeDefinition.forString("Hello world", AttributePersistence.TRANSIENT));
		AttributeMap.define("boolean_test", AttributeDefinition.forBoolean(true, AttributePersistence.TRANSIENT));
		AttributeMap.define("int_test", AttributeDefinition.forInt(Integer.MAX_VALUE, AttributePersistence.TRANSIENT));
		AttributeMap.define("long_test", AttributeDefinition.forLong(Long.MAX_VALUE, AttributePersistence.TRANSIENT));
		AttributeMap.define("double_test", AttributeDefinition.forDouble(Double.MAX_VALUE, AttributePersistence.TRANSIENT));
	}

	/**
	 * Tests Attribute <code>String</code> encoding.
	 */
	@Test
	public void stringEncode() {
		AttributeMap map = new AttributeMap();
		byte[] expected = "Hello world\0".getBytes(StandardCharsets.UTF_8);
		Assert.assertArrayEquals(expected, map.get("string_test").encode());
	}

	/**
	 * Tests Attribute <code>boolean</code> encoding.
	 */
	@Test
	public void booleanEncode() {
		AttributeMap map = new AttributeMap();
		byte[] expected = { 1 };
		Assert.assertArrayEquals(expected, map.get("boolean_test").encode());
	}

	/**
	 * Tests Attribute <code>int</code> encoding.
	 */
	@Test
	public void intEncode() {
		AttributeMap map = new AttributeMap();
		byte[] expected = Longs.toByteArray(Integer.MAX_VALUE);
		Assert.assertArrayEquals(expected, map.get("int_test").encode());
	}

	/**
	 * Tests Attribute <code>long</code> encoding.
	 */
	@Test
	public void longEncode() {
		AttributeMap map = new AttributeMap();
		byte[] expected = Longs.toByteArray(Long.MAX_VALUE);
		Assert.assertArrayEquals(expected, map.get("long_test").encode());
	}

	/**
	 * Tests Attribute <code>double</code> encoding.
	 */
	@Test
	public void doubleEncode() {
		AttributeMap map = new AttributeMap();
		byte[] expected = Longs.toByteArray(Double.doubleToLongBits(Double.MAX_VALUE));
		Assert.assertArrayEquals(expected, map.get("double_test").encode());
	}

}
