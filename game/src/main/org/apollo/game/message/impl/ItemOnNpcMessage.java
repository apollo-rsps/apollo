package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player uses an item on a npc.
 * 
 * @author Lmctruck30
 */

public final class ItemOnNpcMessage extends Message {

	private int itemId;
	
	private int npcIndex;
	
	private int itemSlot;
	
	private int interfaceId;
	
	public ItemOnNpcMessage(int itemId, int npcIndex, int itemSlot, int interfaceId) {
		this.itemId = itemId;
		this.itemSlot = itemSlot;
		this.npcIndex = npcIndex;
		this.interfaceId = interfaceId;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int getItemSlot() {
		return itemSlot;
	}
	
	public int getNpcIndex() {
		return npcIndex;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}

}
