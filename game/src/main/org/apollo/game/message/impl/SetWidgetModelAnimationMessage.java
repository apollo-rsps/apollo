package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed mob's animation.
 *
 * @author Chris Fletcher
 */
public final class SetWidgetModelAnimationMessage extends Message {

	/**
	 * The model's animation id.
	 */
	private final int animation;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new set interface npc model's animation message.
	 *
	 * @param interfaceId The interface id.
	 * @param animation The model's animation id.
	 */
	public SetWidgetModelAnimationMessage(int interfaceId, int animation) {
		this.interfaceId = interfaceId;
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
	public int getInterfaceId() {
		return interfaceId;
	}

}