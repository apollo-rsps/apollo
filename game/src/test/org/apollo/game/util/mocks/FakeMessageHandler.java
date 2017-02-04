package org.apollo.game.util.mocks;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * @author Lesley
 */
public class FakeMessageHandler extends MessageHandler<FakeMessage> {

	public Player lastHandlePlayer;
	public FakeMessage lastHandleMessage;

	public FakeMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, FakeMessage message) {
		lastHandlePlayer = player;
		lastHandleMessage = message;
	}

}
