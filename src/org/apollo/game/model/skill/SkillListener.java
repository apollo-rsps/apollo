package org.apollo.game.model.skill;

import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;

/**
 * An interface which listens to events from a {@link SkillSet}.
 * @author Graham
 */
public interface SkillListener {

	/**
	 * Called when a single skill is updated.
	 * @param set The skill set.
	 * @param id The skill's id.
	 * @param skill The skill.
	 */
	public void skillUpdated(SkillSet set, int id, Skill skill);

	/**
	 * Called when all the skills are updated.
	 * @param set The skill set.
	 */
	public void skillsUpdated(SkillSet set);

	/**
	 * Called when a skill is levelled up.
	 * @param set The skill set.
	 * @param id The skill's id.
	 * @param skill The skill.
	 */
	public void levelledUp(SkillSet set, int id, Skill skill);

}
