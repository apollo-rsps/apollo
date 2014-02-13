package org.apollo.game.model.settings;

/**
 * Represents the status of the friend server.
 * 
 * @author Major
 */
public enum ServerStatus {

	/**
	 * Indicates the friend server is offline.
	 */
	OFFLINE(0),

	/**
	 * Indicates the friend server is being connected to.
	 */
	CONNECTING(1),

	/**
	 * Indicates the friend server is online and connected.
	 */
	ONLINE(2);

	/**
	 * The code of the server status.
	 */
	private final int code;

	/**
	 * Creates a new server status.
	 * 
	 * @param code The code.
	 */
	private ServerStatus(int code) {
		this.code = code;
	}

	/**
	 * Gets the code of this server status.
	 * 
	 * @return The code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets the server status for the specified numerical value.
	 * 
	 * @param value The value.
	 * @return The server status.
	 * @throws IndexOutOfBoundsException If the specified value is out of bounds.
	 */
	public static ServerStatus valueOf(int value) {
		ServerStatus[] values = values();
		if (value < 0 || value >= values.length) {
			throw new IndexOutOfBoundsException("Invalid server status integer value supplied " + value + ".");
		}
		return values[value];
	}

}