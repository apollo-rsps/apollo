package org.apollo.game.sync.block;

/**
 * The force chat {@link SynchronizationBlock}. Both players and npcs can utilise this block. It is not possible to add
 * colour or effect (e.g. wave or scroll) to this block.
 *
 * @author Major
 */
public final class ForceChatBlock extends SynchronizationBlock {

	/**
	 * The chat text.
	 */
	private final String message;

	/**
	 * Creates the force chat block.
	 *
	 * @param message The message.
	 */
	ForceChatBlock(String message) {
		this.message = message;
	}

	/**
	 * Gets the message being sent by this block.
	 *
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

}