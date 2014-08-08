package org.apollo.game.message.handler.impl;

import org.apollo.game.command.Command;
import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.CommandMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that dispatches {@link CommandMessage}s.
 * 
 * @author Graham
 */
public final class CommandMessageHandler extends MessageHandler<CommandMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, CommandMessage message) {
		String[] components = message.getCommand().split(" ");
		String name = components[0];

		String[] arguments = new String[components.length - 1];
		System.arraycopy(components, 1, arguments, 0, arguments.length);

		Command command = new Command(name, arguments);
		World.getWorld().getCommandDispatcher().dispatch(player, command);
	}

}