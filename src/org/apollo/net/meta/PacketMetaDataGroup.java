package org.apollo.net.meta;

/**
 * A class which contains a group of {@link PacketMetaData} objects.
 * @author Graham
 */
public final class PacketMetaDataGroup {

	/**
	 * Creates a {@link PacketMetaDataGroup} from the packet length array.
	 * @param lengthArray The packet length array.
	 * @return The {@link PacketMetaDataGroup} object.
	 * @throws IllegalArgumentException if the array length is not 256 or if
	 * there is an element in the array with a value below -3.
	 */
	public static PacketMetaDataGroup createFromArray(int[] lengthArray) {
		if (lengthArray.length != 256) {
			throw new IllegalArgumentException();
		}
		PacketMetaDataGroup grp = new PacketMetaDataGroup();
		for (int i = 0; i < lengthArray.length; i++) {
			int length = lengthArray[i];
			PacketMetaData metaData = null;
			if (length < -3) {
				throw new IllegalArgumentException();
			} else if (length == -2) {
				metaData = PacketMetaData.createVariableShort();
			} else if (length == -1) {
				metaData = PacketMetaData.createVariableByte();
			} else {
				metaData = PacketMetaData.createFixed(length);
			}
			grp.packets[i] = metaData;
		}
		return grp;
	}

	/**
	 * The array of {@link PacketMetaData} objects.
	 */
	private final PacketMetaData[] packets = new PacketMetaData[256];

	/**
	 * This constructor should not be called directly. Use the
	 * {@link #createFromArray(int[])} method instead.
	 */
	private PacketMetaDataGroup() {

	}

	/**
	 * Gets the meta data for the specified packet.
	 * @param opcode The opcode of the packet.
	 * @return The {@link PacketMetaData}, or {@code null} if the packet does
	 * not exist.
	 * @throws IllegalArgumentException if the opcoe is not in the range 0 to
	 * 255.
	 */
	public PacketMetaData getMetaData(int opcode) {
		if (opcode < 0 || opcode >= packets.length) {
			throw new IllegalArgumentException();
		}
		return packets[opcode];
	}

}
