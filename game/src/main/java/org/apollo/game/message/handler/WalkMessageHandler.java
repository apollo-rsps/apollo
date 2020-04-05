package org.apollo.game.message.handler;

import org.apollo.game.message.impl.decode.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.WalkingQueue;

/**
 * A {@link MessageHandler} that handles {@link WalkMessage}s.
 *
 * @author Graham
 */
public final class WalkMessageHandler extends MessageHandler<WalkMessage> {

	/**
	 * Creates the WalkMessageHandler.
	 *
	 * @param world The {@link World} the {@link WalkMessage} occurred in.
	 */
	public WalkMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, WalkMessage message) {
		WalkingQueue queue = player.getWalkingQueue();
		queue.setRunning(message.isRunning() || player.isRunning());

		queue.addFirstStep(new Position(message.getX(), message.getY(), player.getPosition().getHeight()));

		player.getInterfaceSet().close();

		if (queue.size() > 0) {
			player.stopAction();
		}

		if (player.getInteractingMob() != null) {
			player.resetInteractingMob();
		}
	}

}