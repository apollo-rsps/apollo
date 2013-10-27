package org.apollo.game.command;

/**
 * Represents a command.
 * @author Graham
 */
public final class Command {

	/**
	 * The name of the command.
	 */
	private final String name;

	/**
	 * The command's arguments.
	 */
	private final String[] arguments;

	/**
	 * Creates the command.
	 * @param name The name of the command.
	 * @param arguments The command's arguments.
	 */
	public Command(String name, String[] arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	/**
	 * Gets the name of the command.
	 * @return The name of the command.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the command's arguments.
	 * @return The command's arguments.
	 */
	public String[] getArguments() {
		return arguments;
	}

}
