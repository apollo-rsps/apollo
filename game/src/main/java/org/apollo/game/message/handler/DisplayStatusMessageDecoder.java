package org.apollo.game.message.handler;

import org.apollo.game.message.impl.decode.DisplayStatusMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

public class DisplayStatusMessageDecoder extends MessageHandler<DisplayStatusMessage> {
	/**
	 * Creates the MessageListener.
	 *
	 * @param world The {@link World} the {@link DisplayStatusMessage} occurred in.
	 */
	public DisplayStatusMessageDecoder(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, DisplayStatusMessage message) {
		player.getInterfaceSet().openTop(message.getMode());
	}
}
