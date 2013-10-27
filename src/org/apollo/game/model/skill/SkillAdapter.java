package org.apollo.game.model.skill;

import org.apollo.game.model.Skill;
import org.apollo.game.model.SkillSet;

/**
 * An adapter for the {@link SkillListener}.
 * @author Graham
 */
public abstract class SkillAdapter implements SkillListener {

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {
		/* empty */
	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {
		/* empty */
	}

	@Override
	public void skillsUpdated(SkillSet set) {
		/* empty */
	}

}
