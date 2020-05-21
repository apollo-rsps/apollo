package org.apollo.game.model.skill;

import java.util.stream.IntStream;

import org.apollo.game.message.impl.UpdateSkillMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * A {@link SkillListener} which synchronizes the state of a {@link SkillSet} with a client.
 *
 * @author Graham
 */
public final class SynchronizationSkillListener extends SkillAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the skill synchronization listener.
	 *
	 * @param player The player.
	 */
	public SynchronizationSkillListener(Player player) {
		this.player = player;
	}

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		if (Skill.isCombatSkill(id)) {
			player.updateAppearance();
		}
	}

	@Override
	public void skillsUpdated(SkillSet set) {
		IntStream.range(0, set.size()).forEach(id -> skillUpdated(set, id, set.getSkill(id)));
	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		player.send(new UpdateSkillMessage(id, skill));
	}

}