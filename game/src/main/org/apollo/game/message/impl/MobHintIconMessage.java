package org.apollo.game.message.impl;

import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Mob;

/**
 * A {@link HintIconMessage} which displays a hint over an Npc or Player.
 */
public final class MobHintIconMessage extends HintIconMessage {

	/**
	 * Gets the Type of HintIcon for the specified EntityType.
	 *
	 * @param entityType The EntityType.
	 * @return The HintIcon Type for the EntityType.
	 */
	private static Type fromEntityType(EntityType entityType) {
		switch (entityType) {
			case NPC:
				return Type.NPC;
			case PLAYER:
				return Type.PLAYER;
			default:
				throw new UnsupportedOperationException("Only Mob entities are supported.");
		}
	}

	/**
	 * Creates a new {@link MobHintIconMessage} for the specified Mob.
	 *
	 * @param mob The Mob who will have the HintIcon.
	 * @return The new {@link MobHintIconMessage}, never {@code null}.
	 */
	public static MobHintIconMessage create(Mob mob) {
		return new MobHintIconMessage(fromEntityType(mob.getEntityType()), mob.getIndex());
	}

	/**
	 * Resets the HintIcon for the specified EntityType.
	 *
	 * @param type The EntityType to reset the HintIcon for.
	 * @return The new {@link MobHintIconMessage}, never {@code null}.
	 */
	public static MobHintIconMessage reset(EntityType type) {
		return new MobHintIconMessage(fromEntityType(type), -1);
	}

	/**
	 * The index of the Mob.
	 */
	private final int index;

	/**
	 * Constructs a new {@link MobHintIconMessage} with the specified type.
	 *
	 * @param type The type of HintIcon.
	 * @param index The index of the Mob.
	 */
	private MobHintIconMessage(Type type, int index) {
		super(type);
		this.index = index;
	}

	/**
	 * Gets the index of the Mob.
	 *
	 * @return The index of the Mob.
	 */
	public int getIndex() {
		return index;
	}

}