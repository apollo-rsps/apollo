package org.apollo.game.model.entity;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import org.apollo.game.model.skill.SkillListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * The minimum amounts of experience required for the levels.
	 */
	private static final int[] EXPERIENCE_FOR_LEVEL = new int[100];

	/**
	 * The number of skills.
	 */
	private static final int SKILL_COUNT = 21;

	static {
		int points = 0, output = 0;
		for (int level = 1; level <= 99; level++) {
			EXPERIENCE_FOR_LEVEL[level] = output;
			points += Math.floor(level + 300 * Math.pow(2, level / 7.0));
			output = (int) Math.floor(points / 4);
		}
	}

	/**
	 * Gets the minimum experience required for the specified level.
	 *
	 * @param level The level.
	 * @return The minimum experience.
	 */
	public static int getExperienceForLevel(int level) {
		Preconditions.checkArgument(level >= 1 && level <= 99, "Level must be between 1 and 99, inclusive.");
		return EXPERIENCE_FOR_LEVEL[level];
	}

	/**
	 * Gets the minimum level to get the specified experience.
	 *
	 * @param experience The experience.
	 * @return The minimum level.
	 */
	public static int getLevelForExperience(double experience) {
		Preconditions.checkArgument(experience >= 0 && experience <= MAXIMUM_EXP, "Experience must be between 0 and " + MAXIMUM_EXP + ", inclusive.");
		for (int level = 1; level <= 98; level++) {
			if (experience < EXPERIENCE_FOR_LEVEL[level + 1]) {
				return level;
			}
		}

		return 99;
	}

	/**
	 * The combat level of this skill set.
	 */
	private int combat = 3;

	/**
	 * Whether or not events are being fired.
	 */
	private boolean firingEvents = true;

	/**
	 * The list of skill listeners.
	 */
	private final List<SkillListener> listeners = new ArrayList<>();

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

		double newExperience = Math.min(old.getExperience() + experience, MAXIMUM_EXP);

		int current = old.getCurrentLevel();
		int maximum = getLevelForExperience(newExperience);

		int delta = maximum - old.getMaximumLevel();
		current += delta;

		setSkill(id, new Skill(newExperience, current, maximum));

		if (delta > 0) {
			notifyLevelledUp(id); // here so it notifies using the updated skill
		}
	}

	/**
	 * Adds a {@link SkillListener} to this set.
	 *
	 * @param listener The listener.
	 */
	public void addListener(SkillListener listener) {
		listeners.add(listener);
	}

	/**
	 * Calculates the combat level for this skill set.
	 */
	public void calculateCombatLevel() {
		int attack = skills[Skill.ATTACK].getMaximumLevel();
		int defence = skills[Skill.DEFENCE].getMaximumLevel();
		int strength = skills[Skill.STRENGTH].getMaximumLevel();
		int hitpoints = skills[Skill.HITPOINTS].getMaximumLevel();
		int prayer = skills[Skill.PRAYER].getMaximumLevel();
		int ranged = skills[Skill.RANGED].getMaximumLevel();
		int magic = skills[Skill.MAGIC].getMaximumLevel();

		double base = Ints.max(strength + attack, magic * 2, ranged * 2);
		double combat = (base * 1.3 + defence + hitpoints + prayer / 2) / 4;

		this.combat = (int) combat;
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
		return combat;
	}

	/**
	 * Gets the current level of the specified skill.
	 *
	 * @param skill The skill.
	 * @return The current level.
	 */
	public int getCurrentLevel(int skill) {
		return getSkill(skill).getCurrentLevel();
	}

	/**
	 * Gets the experience of the specified skill.
	 *
	 * @param skill The skill.
	 * @return The experience.
	 */
	public double getExperience(int skill) {
		return getSkill(skill).getExperience();
	}

	/**
	 * Gets the maximum level of the specified skill.
	 *
	 * @param skill The skill.
	 * @return The maximum level.
	 */
	public int getMaximumLevel(int skill) {
		return getSkill(skill).getMaximumLevel();
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
		return Arrays.stream(skills).mapToInt(Skill::getMaximumLevel).sum();
	}

	/**
	 * Normalizes the skills in this set.
	 */
	public void normalize() {
		for (int id = 0; id < skills.length; id++) {
			int current = skills[id].getCurrentLevel(), max = skills[id].getMaximumLevel();

			if (current == max || id == Skill.PRAYER) {
				continue;
			}

			current += current < max ? 1 : -1;
			setSkill(id, new Skill(skills[id].getExperience(), current, max));
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
	public void removeListener(SkillListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Sets the current level of the specified skill.
	 *
	 * @param skill The skill.
	 * @param level The level.
	 */
	public void setCurrentLevel(int skill, int level) {
		Skill old = getSkill(skill);
		setSkill(skill, Skill.updateCurrentLevel(level, old));
	}

	/**
	 * Sets the experience level of the specified skill.
	 *
	 * @param skill The skill.
	 * @param experience The experience.
	 */
	public void setExperience(int skill, double experience) {
		Skill old = getSkill(skill);
		setSkill(skill, Skill.updateExperience(experience, old));
	}

	/**
	 * Sets the maximum level of the specified skill.
	 *
	 * @param skill The skill.
	 * @param level The level.
	 */
	public void setMaximumLevel(int skill, int level) {
		Skill old = getSkill(skill);
		setSkill(skill, Skill.updateMaximumLevel(level, old));
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

	/**
	 * Checks the bounds of the id.
	 *
	 * @param id The id.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	private void checkBounds(int id) {
		Preconditions.checkElementIndex(id, skills.length, "Skill id is out of bounds.");
	}

	/**
	 * Initialises the skill set.
	 */
	private void init() {
		Arrays.setAll(skills, id -> id == Skill.HITPOINTS ? new Skill(1154, 10, 10) : new Skill(0, 1, 1));
	}

	/**
	 * Notifies listeners that a skill has been levelled up.
	 *
	 * @param id The skill's id.
	 */
	private void notifyLevelledUp(int id) {
		checkBounds(id);
		if (firingEvents) {
			listeners.forEach(listener -> listener.levelledUp(this, id, skills[id]));
		}
	}

	/**
	 * Notifies listeners that the skills in this listener have been updated.
	 */
	private void notifySkillsUpdated() {
		if (firingEvents) {
			listeners.forEach(listener -> listener.skillsUpdated(this));
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
			listeners.forEach(listener -> listener.skillUpdated(this, id, skills[id]));
		}
	}

}