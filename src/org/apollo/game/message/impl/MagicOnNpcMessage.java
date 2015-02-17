package org.apollo.game.message.impl;

import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Npc;

/**
 * The magic on npc {@link org.apollo.game.message.impl.MagicOnNpcMessage}
 *
 * @author Stuart
 */
public final class MagicOnNpcMessage extends MagicOnMobMessage {

    /**
     * Creates the magic on npc message
     *
     * @param index   The npc index
     * @param spellId The spell id used
     */
    public MagicOnNpcMessage(int index, int spellId) {
        super(Entity.EntityType.NPC, index, spellId);
    }

}
