package org.apollo.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.skill.SkillListener;

/**
 * Represents the set of the player's skills.
 * 
 * @author Graham
 */
public final class SkillSet {

	/**
	 * The maximum allowed experience.
	 */
	public static final double MAXIMUM_EXP = 200_000_000;

	/**
	 * The number of skills.
	 */
	private static final int SKILL_COUNT = 21;

	/**
	 * Gets the minimum experience required for the specified level.
	 * 
	 * @param level The level.
	 * @return The minimum experience.
	 */
	public static double getExperienceForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Gets the minimum level to get the specified experience.
	 * 
	 * @param experience The experience.
	 * @return The minimum level.
	 */
	public static int getLevelForExperience(double experience) {
		int points = 0, output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= experience + 1) {
				return lvl;
			}
		}
		return 99;
	}

	/**
	 * The combat level for this skill set.
	 */
	private int combatLevel = 3;

	/**
	 * A flag indicating if events are being fired.
	 */
	private boolean firingEvents = true;

	/**
	 * A list of skill listeners.
	 */
	private final List<SkillListener> listeners = new ArrayList<SkillListener>();

	/**
	 * The skills.
	 */
	private final Skill[] skills = new Skill[SKILL_COUNT];

	/**
	 * Creates the skill set.
	 */
	public SkillSet() {
		init();
	}

	/**
	 * Adds experience to the specified skill.
	 * 
	 * @param id The skill id.
	 * @param experience The amount of experience.
	 */
	public void addExperience(int id, double experience) {
		checkBounds(id);

		Skill old = skills[id];

		double newExperience = old.getExperience() + experience;
		newExperience = newExperience > MAXIMUM_EXP ? MAXIMUM_EXP : newExperience;

		int newCurrentLevel = old.getCurrentLevel();
		int newMaximumLevel = getLevelForExperience(newExperience);

		int delta = newMaximumLevel - old.getMaximumLevel();
		newCurrentLevel += delta > 0 ? delta : 0;

		setSkill(id, new Skill(newExperience, newCurrentLevel, newMaximumLevel));

		if (delta > 0) {
			notifyLevelledUp(id); // here so it notifies using the updated skill
		}
	}

	/**
	 * Adds a {@link SkillListener} to this set.
	 * 
	 * @param listener The listener.
	 */
	public boolean addListener(SkillListener listener) {
		return listeners.add(listener);
	}

	/**
	 * Calculates the combat level for this skill set.
	 * 
	 * @return The combat level.
	 */
	public void calculateCombatLevel() {
		int attack = skills[Skill.ATTACK].getMaximumLevel();
		int defence = skills[Skill.DEFENCE].getMaximumLevel();
		int strength = skills[Skill.STRENGTH].getMaximumLevel();
		int hitpoints = skills[Skill.HITPOINTS].getMaximumLevel();
		int prayer = skills[Skill.PRAYER].getMaximumLevel();
		int ranged = skills[Skill.RANGED].getMaximumLevel();
		int magic = skills[Skill.MAGIC].getMaximumLevel();

		double base = (defence + hitpoints + Math.floor(prayer / 2)) * 0.25;
		double melee = (attack + strength) * 0.325;
		double range = ranged * 0.4875;
		double mage = magic * 0.4875;

		this.combatLevel = (int) (base + Math.max(melee, Math.max(range, mage)));
	}

	/**
	 * Checks the bounds of the id.
	 * 
	 * @param id The id.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	private void checkBounds(int id) {
		if (id < 0 || id >= skills.length) {
			throw new IndexOutOfBoundsException("skill id is out of bounds");
		}
	}

	/**
	 * Forces this skill set to refresh.
	 */
	public void forceRefresh() {
		notifySkillsUpdated();
	}

	/**
	 * Gets the combat level of this skill set.
	 * 
	 * @return The combat level.
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets a skill by its id.
	 * 
	 * @param id The id.
	 * @return The skill.
	 */
	public Skill getSkill(int id) {
		checkBounds(id);
		return skills[id];
	}

	/**
	 * Gets the total level for this skill set.
	 * 
	 * @return The total level.
	 */
	public int getTotalLevel() {
		int total = 0;
		for (Skill skill : skills) {
			total += skill.getMaximumLevel();
		}
		return total;
	}

	/**
	 * Initialises the skill set.
	 */
	private void init() {
		for (int id = 0; id < skills.length; id++) {
			skills[id] = (id == Skill.HITPOINTS ? new Skill(1154, 10, 10) : new Skill(0, 1, 1));
		}
	}

	/**
	 * Normalizes the skills in this set.
	 */
	public void normalize() {
		for (int id = 0; id < skills.length; id++) {
			int current = skills[id].getCurrentLevel();
			int max = skills[id].getMaximumLevel();

			if (current == max) {
				continue;
			}
			current += current < max ? 1 : -1;

			setSkill(id, new Skill(skills[id].getExperience(), current, max));
		}
	}

	/**
	 * Notifies listeners that a skill has been levelled up.
	 * 
	 * @param id The skill's id.
	 */
	private void notifyLevelledUp(int id) {
		checkBounds(id);
		if (firingEvents) {
			for (SkillListener listener : listeners) {
				listener.levelledUp(this, id, skills[id]);
			}
		}
	}

	/**
	 * Notifies listeners that the skills in this listener have been updated.
	 */
	private void notifySkillsUpdated() {
		if (firingEvents) {
			for (SkillListener listener : listeners) {
				listener.skillsUpdated(this);
			}
		}
	}

	/**
	 * Notifies listeners that a skill has been updated.
	 * 
	 * @param id The skill's id.
	 */
	private void notifySkillUpdated(int id) {
		checkBounds(id);
		if (firingEvents) {
			for (SkillListener listener : listeners) {
				listener.skillUpdated(this, id, skills[id]);
			}
		}
	}

	/**
	 * Removes all the {@link SkillListener}s.
	 */
	public void removeAllListeners() {
		listeners.clear();
	}

	/**
	 * Removes a {@link SkillListener}.
	 * 
	 * @param listener The listener to remove.
	 */
	public boolean removeListener(SkillListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Sets a {@link Skill}.
	 * 
	 * @param id The id.
	 * @param skill The skill.
	 */
	public void setSkill(int id, Skill skill) {
		checkBounds(id);
		skills[id] = skill;
		notifySkillUpdated(id);
	}

	/**
	 * Gets the number of {@link Skill}s in this set.
	 * 
	 * @return The number of skills.
	 */
	public int size() {
		return skills.length;
	}

	/**
	 * Starts the firing of events.
	 */
	public void startFiringEvents() {
		firingEvents = true;
	}

	/**
	 * Stops events from being fired.
	 */
	public void stopFiringEvents() {
		firingEvents = false;
	}

}