package org.apollo.game.command;

import org.apollo.game.model.Player;
import org.apollo.game.model.Player.PrivilegeLevel;

/**
 * A {@link CommandListener} which checks the {@link PrivilegeLevel} of the
 * {@link Player} executing the command.
 * @author Graham
 */
public abstract class PrivilegedCommandListener implements CommandListener {

	/**
	 * The minimum privilege level.
	 */
	private final PrivilegeLevel level;

	/**
	 * Creates the privileged command listener with the specified minimum level.
	 * @param level The minimum privilege level.
	 */
	public PrivilegedCommandListener(PrivilegeLevel level) {
		this.level = level;
	}

	/**
	 * Executes a privileged command.
	 * @param player The player.
	 * @param command The command.
	 */
	public abstract void executePrivileged(Player player, Command command);

	@Override
	public final void execute(Player player, Command command) {
		if (player.getPrivilegeLevel().toInteger() >= level.toInteger()) {
			executePrivileged(player, command);
		}
	}

}
