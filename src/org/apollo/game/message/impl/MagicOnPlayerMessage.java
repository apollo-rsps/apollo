package org.apollo.game.message.impl;

import org.apollo.game.model.entity.EntityType;

/**
 * The Player {@link MagicOnMobMessage}.
 *
 * @author Stuart
 */
public final class MagicOnPlayerMessage extends MagicOnMobMessage {

	/**
	 * Creates the MagicOnPlayerMessage.
	 *
	 * @param index The index of the player.
	 * @param spellId The spell id used.
	 */
	public MagicOnPlayerMessage(int index, int spellId) {
		super(EntityType.PLAYER, index, spellId);
	}

}