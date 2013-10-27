package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Skill;

/**
 * An {@link Event} sent to the client to update a player's skill level.
 * @author Graham
 */
public final class UpdateSkillEvent extends Event {

	/**
	 * The skill's id.
	 */
	private final int id;

	/**
	 * The skill.
	 */
	private final Skill skill;

	/**
	 * Creates an update skill event.
	 * @param id The id.
	 * @param skill The skill.
	 */
	public UpdateSkillEvent(int id, Skill skill) {
		this.id = id;
		this.skill = skill;
	}

	/**
	 * Gets the skill's id.
	 * @return The skill's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the skill.
	 * @return The skill.
	 */
	public Skill getSkill() {
		return skill;
	}

}
