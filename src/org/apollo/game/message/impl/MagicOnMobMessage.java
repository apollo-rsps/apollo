package org.apollo.game.message.impl;

import org.apollo.game.message.Message;
import org.apollo.game.model.entity.Entity;

/**
 * A {@link Message} sent by the client representing when a player uses any type of magic spell on a mob.
 *
 * @author Stuart
 */
public abstract class MagicOnMobMessage extends Message {

    /**
     * The type of the mob
     */
    private final Entity.EntityType type;
    /**
     * The index of the mob
     */
    private final int index;
    /**
     * The spell if used
     */
    private final int spellId;

    /**
     * Creates a magic on mob message
     *
     * @param type    The mob type
     * @param index   The mob index
     * @param spellId The spell id
     */
    public MagicOnMobMessage(Entity.EntityType type, int index, int spellId) {
        this.type = type;
        this.index = index;
        this.spellId = spellId;
    }

    /**
     * Gets the type of the mob the spell is being used on
     *
     * @return The mob type
     */
    public Entity.EntityType getType() {
        return type;
    }

    /**
     * Gets the index of the mob the spell is being used on
     *
     * @return The mob index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the spell id that is being used
     *
     * @return The spell id
     */
    public int getSpellId() {
        return spellId;
    }

}