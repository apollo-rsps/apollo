package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.WalkMessage;
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
	public void handle(MessageHandlerContext ctx, Player player, WalkMessage message) {
		WalkingQueue queue = player.getWalkingQueue();

		Position[] steps = message.getSteps();
		for (int index = 0; index < steps.length; index++) {
			Position step = steps[index];
			if (index == 0) {
				if (!queue.addFirstStep(step)) {
					return; // ignore packet
				}
			} else {
				queue.addStep(step);
			}
		}

		queue.setRunningQueue(message.isRunning() || player.isRunning());
		player.getInterfaceSet().close();

		if (queue.size() > 0) {
			player.stopAction();
		}

		if (player.getInteractingMob() != null) {
			player.resetInteractingMob();
		}
	}

}