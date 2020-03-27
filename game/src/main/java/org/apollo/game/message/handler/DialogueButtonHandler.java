package org.apollo.game.message.handler;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.ServerInterfaceType;

/**
 * A {@link MessageHandler} which intercepts button clicks on dialogues, and forwards the message to the current
 * listener.
 *
 * @author Chris Fletcher
 */
public final class DialogueButtonHandler extends MessageHandler<IfActionMessage> {

	/**
	 * Creates the DialogueButtonHandler.
	 *
	 * @param world The {@link World} the {@link IfActionMessage} occurred in.
	 */
	public DialogueButtonHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, IfActionMessage message) {
		if (player.getInterfaceSet().contains(ServerInterfaceType.DIALOGUE)) {
			boolean terminate = player.getInterfaceSet().buttonClicked(message.getComponentId());

			if (terminate) {
				message.terminate();
			}
		}
	}

}