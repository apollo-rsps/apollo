package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ClosedInterfaceMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} for the {@link ClosedInterfaceMessage}.
 *
 * @author Graham
 */
public final class ClosedInterfaceMessageHandler extends MessageHandler<ClosedInterfaceMessage> {

	/**
	 * Creates the ClosedInterfaceMessageHandler.
	 *
	 * @param world The {@link World} the {@link ClosedInterfaceMessage} occurred in.
	 */
	public ClosedInterfaceMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ClosedInterfaceMessage message) {
		player.getInterfaceSet().interfaceClosed();
	}

}