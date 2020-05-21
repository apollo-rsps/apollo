package org.apollo.game.message.impl.encode;

import com.google.common.base.Strings;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to display a server chat message.
 *
 * @author Graham
 */
public final class ServerChatMessage extends Message {

	public enum ChatType {
		GAME_MESSAGE(0, false),
		MOD_CHAT(1, false),
		PUBLIC_CHAT(2, false),
		PRIVATE_CHAT(3, false),
		ENGINE(4, false),
		LOGIN_LOGOUT_NOTIFICATION(5, false),
		PRIVATE_CHAT_OUT(6, false),
		MOD_PRIVATE_CHAT(7, false),
		FRIENDS_CHAT(9, false),
		FRIENDS_CHAT_NOTIFICATION(11, false),
		BROADCAST(14, false),
		SNAPSHOT_FEEDBACK(26, false),
		ITEM_EXAMINE(27, false),
		NPC_EXAMINE(28, false),
		LOC_EXAMINE(29, false),
		FRIEND_NOTIFICATION(30, false),
		IGNORE_NOTIFICATION(31, false),
		AUTO_TYPER(90, false),
		MOD_AUTO_TYPER(91, false),
		CONSOLE(99, false),
		TRADE_REQ(101, true),
		TRADE(102, false),
		CHAL_REQ_TRADE(103, true),
		CHAL_REQ_FRIENDS_CHAT(104, false),
		SPAM(105, false),
		PLAYER_RELATED(106, false),
		TEN_SECOND_TIMOUT(107, false),
		;

		private final int id;
		private final boolean extensionAvailable;

		ChatType(int id, boolean extensionAvailable) {
			this.id = id;
			this.extensionAvailable = extensionAvailable;
		}

		public int getId() {
			return id;
		}

		public boolean isExtensionAvailable() {
			return extensionAvailable;
		}
	}

	/**
	 * The message to send.
	 */
	private final String message;
	private final ChatType type;
	private final String extension;

	public ServerChatMessage(String message, ChatType type, String extension) {
		if (!type.isExtensionAvailable() && !extension.isEmpty()) {
			throw new IllegalArgumentException("Type of " + type + " cannot contain an extension.");
		}
		this.message = message;
		this.type = type;
		this.extension = Strings.nullToEmpty(extension);
	}

	public ServerChatMessage(String message, ChatType type) {
		this(message, type, "");
	}

	public ServerChatMessage(String message) {
		this(message, ChatType.GAME_MESSAGE);
	}

	public String getMessage() {
		return message;
	}

	public ChatType getType() {
		return type;
	}

	public String getExtension() {
		return extension;
	}
}