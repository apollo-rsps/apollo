package org.apollo.game.message.impl;

import org.apollo.game.model.entity.Skill;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to update a player's skill level.
 *
 * @author Graham
 */
public final class UpdateSkillMessage extends Message {

	/**
	 * The skill's id.
	 */
	private final int id;

	/**
	 * The skill.
	 */
	private final Skill skill;

	/**
	 * Creates an update skill message.
	 *
	 * @param id The id.
	 * @param skill The skill.
	 */
	public UpdateSkillMessage(int id, Skill skill) {
		this.id = id;
		this.skill = skill;
	}

	/**
	 * Gets the skill's id.
	 *
	 * @return The skill's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the skill.
	 *
	 * @return The skill.
	 */
	public Skill getSkill() {
		return skill;
	}

}