package org.apollo.game.message.handler;

import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.game.message.impl.PlayerDesignMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that handles {@link PlayerDesignMessage}s.
 *
 * @author Graham
 */
public final class PlayerDesignMessageHandler extends MessageHandler<PlayerDesignMessage> {

	/**
	 * Creates the PlayerDesignMessageHandler.
	 *
	 * @param world The {@link World} the {@link PlayerDesignMessage} occurred in.
	 */
	public PlayerDesignMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, PlayerDesignMessage message) {
		player.setAppearance(message.getAppearance());
		player.send(new CloseInterfaceMessage());
	}

}