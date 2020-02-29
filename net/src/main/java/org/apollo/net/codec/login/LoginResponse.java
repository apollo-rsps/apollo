package org.apollo.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Represents a login response.
 *
 * @author Graham
 */
public final class LoginResponse {

	/**
	 * The login status.
	 */
	private final int status;

	/**
	 * The payload.
	 */
	private ByteBuf payload;
	/**
	 * Creates the login response.
	 *
	 * @param status The login status.
	 * @param payload The rights payload.
	 */
	public LoginResponse(int status, ByteBuf payload) {
		this.status = status;
		this.payload = payload;
	}

	public LoginResponse(int status) {
		this(status, Unpooled.EMPTY_BUFFER);
	}

	/**
	 * Gets the status.
	 *
	 * @return The status.
	 */
	public int getStatus() {
		return status;
	}

	public ByteBuf getPayload() {
		return payload;
	}
}