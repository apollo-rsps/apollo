package org.apollo.game.event.handler.impl;

import org.apollo.game.command.Command;
import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.CommandEvent;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * An {@link EventHandler} that dispatches {@link CommandEvent}s.
 * 
 * @author Graham
 */
public final class CommandEventHandler extends EventHandler<CommandEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, CommandEvent event) {
	String[] components = event.getCommand().split(" ");
	String name = components[0];

	String[] arguments = new String[components.length - 1];
	System.arraycopy(components, 1, arguments, 0, arguments.length);

	Command command = new Command(name, arguments);
	World.getWorld().getCommandDispatcher().dispatch(player, command);
    }

}