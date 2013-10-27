package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event issued by the client to send a {@code ::} command/
 * @author Graham
 */
public final class CommandEvent extends Event {

	/**
	 * The command.
	 */
	private final String command;

	/**
	 * Creates the command event.
	 * @param command The command.
	 */
	public CommandEvent(String command) {
		this.command = command;
	}

	/**
	 * Gets the command.
	 * @return The command.
	 */
	public String getCommand() {
		return command;
	}

}
