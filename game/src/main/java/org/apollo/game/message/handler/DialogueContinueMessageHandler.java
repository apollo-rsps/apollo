package org.apollo.game.message.handler;

import org.apollo.game.message.impl.DialogueContinueMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.InterfaceType;

/**
 * A {@link MessageHandler} for the {@link DialogueContinueMessage}.
 *
 * @author Chris Fletcher
 */
public final class DialogueContinueMessageHandler extends MessageHandler<DialogueContinueMessage> {

	/**
	 * Creates the DialogueContinueMessageHandler.
	 *
	 * @param world The {@link World} the {@link DialogueContinueMessage} occurred in.
	 */
	public DialogueContinueMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, DialogueContinueMessage message) {
		if (player.getInterfaceSet().contains(InterfaceType.DIALOGUE)) {
			player.getInterfaceSet().continueRequested();
		}
	}

}