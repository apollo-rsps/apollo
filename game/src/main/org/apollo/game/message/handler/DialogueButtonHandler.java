package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.InterfaceType;

/**
 * A {@link MessageHandler} which intercepts button clicks on dialogues, and forwards the message to the current
 * listener.
 *
 * @author Chris Fletcher
 */
public final class DialogueButtonHandler extends MessageHandler<ButtonMessage> {

	/**
	 * Creates the DialogueButtonHandler.
	 *
	 * @param world The {@link World} the {@link ButtonMessage} occurred in.
	 */
	public DialogueButtonHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ButtonMessage message) {
		if (player.getInterfaceSet().contains(InterfaceType.DIALOGUE)) {
			boolean terminate = player.getInterfaceSet().buttonClicked(message.getWidgetId());

			if (terminate) {
				message.terminate();
			}
		}
	}

}