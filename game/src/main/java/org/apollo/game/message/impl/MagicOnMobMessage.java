package org.apollo.game.message.impl;

import com.google.common.base.MoreObjects;
import org.apollo.game.model.entity.EntityType;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a Player uses a magic spell on a Mob.
 *
 * @author Stuart
 */
public abstract class MagicOnMobMessage extends Message {

	/**
	 * The type of the Mob.
	 */
	private final EntityType type;

	/**
	 * The index of the Mob.
	 */
	private final int index;

	/**
	 * The spell id used.
	 */
	private final int spellId;

	/**
	 * Creates the MagicOnMobMessage.
	 *
	 * @param type The Mob type.
	 * @param index The Mob index.
	 * @param spellId The spell id.
	 */
	public MagicOnMobMessage(EntityType type, int index, int spellId) {
		this.type = type;
		this.index = index;
		this.spellId = spellId;
	}

	/**
	 * Gets the type of the Mob the spell is being used on.
	 *
	 * @return The Mob type.
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * Gets the index of the Mob the spell is being used on.
	 *
	 * @return The Mob index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the spell id that is being used.
	 *
	 * @return The spell id.
	 */
	public int getSpellId() {
		return spellId;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", getType()).add("index", getIndex()).add("spellId", getSpellId()).toString();
	}
}