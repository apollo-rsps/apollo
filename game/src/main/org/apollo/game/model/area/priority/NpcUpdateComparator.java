package org.apollo.game.model.area.priority;

import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;

/**
 * An {@link UpdateComparator} implementation that will prioritize based on
 * characteristics specific to {@link Npc}s.
 * 
 * @author lare96
 */
public final class NpcUpdateComparator extends UpdateComparator<Npc> {

	/**
	 * Creates a new {@link NpcUpdateComparator}.
	 * 
	 * @param player The player that is operating this comparator.
	 */
	public NpcUpdateComparator(Player player) {
		super(player);
	}

	@Override
	public void andCompare(Npc o1, Npc o2) {
		// XXX Sort by combat level?
		// XXX Sort by NPCs that are assigned to you? (pets, barrows brothers,
		// etc.)
	}
}
