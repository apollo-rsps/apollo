package org.apollo.game.message.handler;

import org.apollo.game.message.impl.EnteredAmountMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} for the {@link EnteredAmountMessage}.
 *
 * @author Graham
 */
public final class EnteredAmountMessageHandler extends MessageHandler<EnteredAmountMessage> {

	/**
	 * Creates the EnteredAmountMessageHandler.
	 *
	 * @param world The {@link World} the {@link EnteredAmountMessage} occurred in.
	 */
	public EnteredAmountMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, EnteredAmountMessage message) {
		player.getInterfaceSet().enteredAmount(message.getAmount());
	}

}