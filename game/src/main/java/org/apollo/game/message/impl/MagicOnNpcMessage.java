package org.apollo.game.message.impl;

import org.apollo.game.model.entity.EntityType;

/**
 * The magic on npc {@link MagicOnNpcMessage}
 *
 * @author Stuart
 */
public final class MagicOnNpcMessage extends MagicOnMobMessage {

	/**
	 * Creates the MagicOnMobMessage.
	 *
	 * @param index The Npc index.
	 * @param spellId The spell id used.
	 */
	public MagicOnNpcMessage(int index, int spellId) {
		super(EntityType.NPC, index, spellId);
	}

}
