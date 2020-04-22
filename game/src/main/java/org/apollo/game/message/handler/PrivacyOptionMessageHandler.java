package org.apollo.game.message.handler;

import org.apollo.game.message.impl.decode.PrivacyOptionMessage;
import org.apollo.game.message.impl.encode.ChatFilterSettingsMessage;
import org.apollo.game.message.impl.encode.SetPrivateChatFilterMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;

/**
 * @author Khaled Abdeljaber
 */
public class PrivacyOptionMessageHandler extends MessageHandler<PrivacyOptionMessage> {

	/**
	 * Creates the MessageListener.
	 *
	 * @param world The {@link World} the {@link PrivacyOptionMessage} occurred in.
	 */
	public PrivacyOptionMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, PrivacyOptionMessage message) {
		var chat = message.getChatPrivacy();
		var friend = message.getFriendPrivacy();
		var trade = message.getTradePrivacy();

		player.send(new ChatFilterSettingsMessage(chat, trade));
		player.send(new SetPrivateChatFilterMessage(friend));
	}
}
