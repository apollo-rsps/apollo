package org.apollo.game.message.handler;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that responds to {@link IfActionMessage}s for withdrawing items as notes.
 *
 * @author Graham
 */
public final class BankButtonMessageHandler extends MessageHandler<IfActionMessage> {

	/**
	 * Creates the BankButtonMessageHandler.
	 *
	 * @param world The {@link World} the {@link IfActionMessage} occurred in.
	 */
	public BankButtonMessageHandler(World world) {
		super(world);
	}

	/**
	 * The withdraw as item button id.
	 */
	private static final int WITHDRAW_AS_ITEM = 5387;

	/**
	 * The withdraw as note button id.
	 */
	private static final int WITHDRAW_AS_NOTE = 5386;

	@Override
	public void handle(Player player, IfActionMessage message) {
		if (message.getComponentId() == WITHDRAW_AS_ITEM) {
			player.setWithdrawingNotes(false);
		} else if (message.getComponentId() == WITHDRAW_AS_NOTE) {
			player.setWithdrawingNotes(true);
		}
	}

}