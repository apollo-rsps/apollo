package org.apollo.game.sync.block;

/**
 * The Force Chat {@link SynchronizationBlock}. This is a block that can be implemented in both player and npc
 * synchronization tasks, and will cause the character to shout the specified text. It is not possible to add colour or
 * effect (e.g. wave or scroll) to this block.
 * 
 * @author Major
 */
public class ForceChatBlock extends SynchronizationBlock {

	/**
	 * The chat text.
	 */
	private final String message;

	/**
	 * Creates a new force chat [@link SynchronizationBlock}.
	 * 
	 * @param message The message the character will say.
	 */
	public ForceChatBlock(String message) {
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