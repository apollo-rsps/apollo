package org.apollo.net.meta;

/**
 * A class which contains meta data for a single type of packet.
 * @author Graham
 */
public final class PacketMetaData {

	/**
	 * Creates a {@link PacketMetaData} object for a fixed-length packet.
	 * @param length The length of the packet.
	 * @return The {@link PacketMetaData} object.
	 * @throws IllegalArgumentException if length is less than 0.
	 */
	public static PacketMetaData createFixed(int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new PacketMetaData(PacketType.FIXED, length);
	}

	/**
	 * Creates a {@link PacketMetaData} object for a variable byte length
	 * packet.
	 * @return The {@link PacketMetaData} object.
	 */
	public static PacketMetaData createVariableByte() {
		return new PacketMetaData(PacketType.VARIABLE_BYTE, 0);
	}

	/**
	 * Creates a {@link PacketMetaData} object for a variable short length
	 * packet.
	 * @return The {@link PacketMetaData} object.
	 */
	public static PacketMetaData createVariableShort() {
		return new PacketMetaData(PacketType.VARIABLE_SHORT, 0);
	}

	/**
	 * The type of packet.
	 */
	private final PacketType type;

	/**
	 * The length of this packet.
	 */
	private final int length;

	/**
	 * Creates the packet meta data object. This should not be called directy.
	 * Use the {@link #createFixed(int)}, {@link #createVariableByte()} and
	 * {@link #createVariableShort()} methods instead!
	 * @param type The type of packet.
	 * @param length The length of the packet.
	 */
	private PacketMetaData(PacketType type, int length) {
		this.type = type;
		this.length = length;
	}

	/**
	 * Gets the type of packet.
	 * @return The type of packet.
	 */
	public PacketType getType() {
		return type;
	}

	/**
	 * Gets the length of this packet.
	 * @return The length of this packet.
	 * @throws IllegalStateException if the packet is not a fixed-size packet.
	 */
	public int getLength() {
		if (type != PacketType.FIXED) {
			throw new IllegalStateException();
		}
		return length;
	}

}
