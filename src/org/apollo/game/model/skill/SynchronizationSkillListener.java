package org.apollo.game.model.skill;

import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * A {@link SkillListener} which synchronizes the state of a {@link SkillSet}
 * with a client.
 * @author Graham
 */
public final class SynchronizationSkillListener extends SkillAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the skill synchronization listener.
	 * @param player The player.
	 */
	public SynchronizationSkillListener(Player player) {
		this.player = player;
	}

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		player.getBlockSet().add(SynchronizationBlock.createAppearanceBlock(player));
	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		player.send(new UpdateSkillEvent(id, skill));
	}

	@Override
	public void skillsUpdated(SkillSet set) {
		for (int id = 0; id < set.size(); id++) {
			player.send(new UpdateSkillEvent(id, set.getSkill(id)));
		}
	}

}
