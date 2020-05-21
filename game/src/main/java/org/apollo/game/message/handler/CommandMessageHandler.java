package org.apollo.game.message.handler;

import org.apollo.game.command.Command;
import org.apollo.game.message.impl.CommandMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

/**
 * A {@link MessageHandler} that dispatches {@link CommandMessage}s.
 *
 * @author Graham
 */
public final class CommandMessageHandler extends MessageHandler<CommandMessage> {

	/**
	 * Creates the CommandMessageHandler.
	 *
	 * @param world The {@link World} the {@link Message} occurred in.
	 */
	public CommandMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, CommandMessage message) {
		String[] components = message.getCommand().split(" ");
		String name = components[0];

		String[] arguments = new String[components.length - 1];
		System.arraycopy(components, 1, arguments, 0, arguments.length);

		Command command = new Command(name, arguments);
		world.getCommandDispatcher().dispatch(player, command);
	}

}