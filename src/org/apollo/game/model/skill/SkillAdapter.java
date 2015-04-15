package org.apollo.game.model.skill;

import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.SkillSet;

/**
 * An adapter for the {@link SkillListener}.
 *
 * @author Graham
 */
public abstract class SkillAdapter implements SkillListener {

	@Override
	public void levelledUp(SkillSet set, int id, Skill skill) {

	}

	@Override
	public void skillsUpdated(SkillSet set) {

	}

	@Override
	public void skillUpdated(SkillSet set, int id, Skill skill) {

	}

}