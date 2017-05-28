package org.apollo.game.message.impl;

import org.apollo.game.model.entity.setting.ServerStatus;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to update the friend server status.
 *
 * @author Major
 */
public final class FriendServerStatusMessage extends Message {

	/**
	 * The status code of the friend server.
	 */
	private final int status;

	/**
	 * Creates a new friend server status message.
	 *
	 * @param status The status.
	 */
	public FriendServerStatusMessage(ServerStatus status) {
		this.status = status.getCode();
	}

	/**
	 * Gets the status code of the friend server.
	 *
	 * @return The status code.
	 */
	public int getStatusCode() {
		return status;
	}

}