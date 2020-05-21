package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed mob's animation.
 *
 * @author Khaled Abdeljaber
 */
public final class IfSetAnimMessage extends Message {

	/**
	 * The model's animation id.
	 */
	private final int animation;

	/**
	 * The interface id.
	 */
	private final int packedInterface;

	/**
	 * Creates a new set interface npc model's animation message.
	 *
	 * @param packedInterface The interface id.
	 * @param animation The model's animation id.
	 */
	public IfSetAnimMessage(int packedInterface, int componentId, int animation) {
		this.packedInterface = packedInterface << 16 | componentId;
		this.animation = animation;
	}

	/**
	 * Gets the model's mood id.
	 *
	 * @return The model's mood id.
	 */
	public int getAnimation() {
		return animation;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getPackedInterface() {
		return packedInterface;
	}

}