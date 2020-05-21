package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to add a player to the friend list.
 *
 * @author Major
 */
public final class SendFriendMessage extends Message {

	/**
	 * The type Friend message component.
	 */
	public static final class FriendMessageComponent {
		/**
		 * The username of the friend.
		 */
		private final String username;

		/**
		 * The world id the friend is in.
		 */
		private final int world;

		/**
		 * Creates a new send friend message.
		 *
		 * @param username The username of the friend.
		 * @param world    The world the friend is in.
		 */
		public FriendMessageComponent(String username, int world) {
			this.username = username;
			this.world = world;
		}

		/**
		 * Gets the username of the friend.
		 *
		 * @return The username.
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * Gets the world id the friend is in.
		 *
		 * @return The world id.
		 */
		public int getWorld() {
			return world;
		}

		/**
		 * Gets the encoded world id to be sent to the client.
		 *
		 * @return The encoded world id.
		 */
		public int getEncodedWorld() {
			return world;
		}
	}

	/**
	 * The components that build the message.
	 */
	private final FriendMessageComponent[] components;

	/**
	 * Instantiates a new Send friend message.
	 *
	 * @param components the components
	 */
	public SendFriendMessage(FriendMessageComponent... components) {
		this.components = components;
	}

	/**
	 * Get components friend message component [ ].
	 *
	 * @return the friend message component [ ]
	 */
	public FriendMessageComponent[] getComponents() {
		return components;
	}
}