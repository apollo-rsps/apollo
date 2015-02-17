package org.apollo.game.message.impl;

import org.apollo.game.model.entity.Entity;

/**
 * The magic on player {@link org.apollo.game.message.impl.MagicOnMobMessage
 *
 * @author Stuart
 */
public final class MagicOnPlayerMessage extends MagicOnMobMessage {

    /**
     * Creates the magic on player message
     *
     * @param index   The player index
     * @param spellId The spell id used
     */
    public MagicOnPlayerMessage(int index, int spellId) {
        super(Entity.EntityType.PLAYER, index, spellId);
    }

}