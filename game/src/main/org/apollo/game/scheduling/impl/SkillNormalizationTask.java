package org.apollo.game.scheduling.impl;

import org.apollo.game.model.entity.Mob;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A {@link ScheduledTask} which normalizes the skills of a player: gradually brings them back to their normal value as
 * specified by the experience.
 *
 * @author Graham
 */
public final class SkillNormalizationTask extends ScheduledTask {

	/**
	 * The mob.
	 */
	private final Mob mob;

	/**
	 * Creates the skill normalization task.
	 *
	 * @param mob The mob.
	 */
	public SkillNormalizationTask(Mob mob) {
		super(100, false);
		this.mob = mob;
	}

	@Override
	public void execute() {
		if (!mob.isActive()) {
			stop();
		} else {
			mob.getSkillSet().normalize();
		}
	}

}