package org.apollo.game.scheduling.impl;

import org.apollo.game.model.Character;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A {@link ScheduledTask} which normalizes the skills of a player: gradually
 * brings them back to their normal value as specified by the experience.
 * @author Graham
 */
public final class SkillNormalizationTask extends ScheduledTask {

	/**
	 * The character.
	 */
	private final Character character;

	/**
	 * Creates the skill normalization task.
	 * @param character The character.
	 */
	public SkillNormalizationTask(Character character) {
		super(100, false);
		this.character = character;
	}

	@Override
	public void execute() {
		if (!character.isActive()) { // TODO is this check okay for this? an NPC could be temporarily removed from list
			stop();
		} else {
			character.getSkillSet().normalize();
		}
	}

}
