package org.apollo.game.command;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An interface which should be implemented to listen to {@link Command}s.
 *
 * @author Graham
 * @author Major
 */
public abstract class CommandListener {

	/**
	 * The minimum privilege level.
	 */
	private final PrivilegeLevel level;

	/**
	 * Creates a new command listener with a {@link PrivilegeLevel#STANDARD} requirement.
	 */
	public CommandListener() {
		this(PrivilegeLevel.STANDARD);
	}

	/**
	 * Creates a new command listener.
	 *
	 * @param level The required {@link PrivilegeLevel}.
	 */
	public CommandListener(PrivilegeLevel level) {
		this.level = level;
	}

	/**
	 * Executes the action for this command.
	 *
	 * @param player The player.
	 * @param command The command.
	 */
	public abstract void execute(Player player, Command command);

	/**
	 * Executes a privileged command.
	 *
	 * @param player The player.
	 * @param command The command.
	 */
	public final void executePrivileged(Player player, Command command) {
		if (player.getPrivilegeLevel().toInteger() >= level.toInteger()) {
			execute(player, command);
		}
	}

}