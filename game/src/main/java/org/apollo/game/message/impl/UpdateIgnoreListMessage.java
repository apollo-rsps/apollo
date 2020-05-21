package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates the ignored user list.
 *
 * @author Major
 */
public final class UpdateIgnoreListMessage extends Message {

	public static final class IgnoreMessageComponent {
		/**
		 * The username of the ignored player.
		 */
		private final String username;

		/**
		 * Creates a new ignore player message.
		 *
		 * @param username The ignored player's username.
		 */
		public IgnoreMessageComponent(String username) {
			this.username = username;
		}

		/**
		 * Gets the username of the ignored player.
		 *
		 * @return The username.
		 */
		public String getUsername() {
			return username;
		}

	}

	/**
	 * The components that build the message.
	 */
	private final IgnoreMessageComponent[] components;

	/**
	 * Instantiates a new Send friend message.
	 *
	 * @param components the components
	 */
	public UpdateIgnoreListMessage(IgnoreMessageComponent... components) {
		this.components = components;
	}

	/**
	 * Get components friend message component [ ].
	 *
	 * @return the friend message component [ ]
	 */
	public IgnoreMessageComponent[] getComponents() {
		return components;
	}
}