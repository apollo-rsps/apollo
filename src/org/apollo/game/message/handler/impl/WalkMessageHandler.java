package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.WalkingQueue;

/**
 * A {@link MessageHandler} that handles {@link WalkMessage}s.
 * 
 * @author Graham
 */
public final class WalkMessageHandler extends MessageHandler<WalkMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, WalkMessage message) {
		WalkingQueue queue = player.getWalkingQueue();

		Position[] steps = message.getSteps();
		for (int i = 0; i < steps.length; i++) {
			Position step = steps[i];
			if (i == 0) {
				if (!queue.addFirstStep(step)) {
					return; // ignore packet
				}
			} else {
				queue.addStep(step);
			}
		}

		queue.setRunningQueue(message.isRunning() || player.isRunning());

		if (queue.size() > 0) {
			player.stopAction();
		}
		player.getInterfaceSet().close();
	}

}