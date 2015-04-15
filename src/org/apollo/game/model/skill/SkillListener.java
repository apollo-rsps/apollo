package org.apollo.game.model.skill;

import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;

/**
 * An interface which listens to events from a {@link SkillSet}.
 *
 * @author Graham
 */
public interface SkillListener {

	/**
	 * Called when a {@link Skill} is levelled up.
	 *
	 * @param set The {@link SkillSet}.
	 * @param id The skill's id.
	 * @param skill The skill.
	 */
	public void levelledUp(SkillSet set, int id, Skill skill);

	/**
	 * Called when all {@link Skill}s are updated.
	 *
	 * @param set The {@link SkillSet}.
	 */
	public void skillsUpdated(SkillSet set);

	/**
	 * Called when a single {@link Skill} is updated.
	 *
	 * @param set The {@link SkillSet}.
	 * @param id The skill's id.
	 * @param skill The skill.
	 */
	public void skillUpdated(SkillSet set, int id, Skill skill);

}