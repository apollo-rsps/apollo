package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed npc model.
 *
 * @author Chris Fletcher
 */
public final class SetWidgetNpcModelMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int interfaceId;

	/**
	 * The model's (NPC) id.
	 */
	private final int modelId;

	/**
	 * Creates a new set interface NPC model message.
	 *
	 * @param interfaceId The interface's id.
	 * @param modelId The model's (NPC) id.
	 */
	public SetWidgetNpcModelMessage(int interfaceId, int modelId) {
		this.interfaceId = interfaceId;
		this.modelId = modelId;
	}

	/**
	 * Gets the interface's id.
	 *
	 * @return The id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the model's (NPC) id.
	 *
	 * @return The id.
	 */
	public int getModelId() {
		return modelId;
	}

}