package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed npc model.
 *
 * @author Khaled Abdeljaber
 */
public final class IfSetNpcHeadMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int packedInterface;

	/**
	 * The model's (NPC) id.
	 */
	private final int npcId;

	/**
	 * Creates a new set interface NPC model message.
	 *
	 * @param interfaceId The interface's id.
	 * @param componentId The component id.
	 * @param npcId The model's (NPC) id.
	 */
	public IfSetNpcHeadMessage(int interfaceId, int componentId, int npcId) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.npcId = npcId;
	}

	/**
	 * Gets the interface's id.
	 *
	 * @return The id.
	 */
	public int getPackedInterface() {
		return packedInterface;
	}

	/**
	 * Gets the model's (NPC) id.
	 *
	 * @return The id.
	 */
	public int getNpcId() {
		return npcId;
	}

}