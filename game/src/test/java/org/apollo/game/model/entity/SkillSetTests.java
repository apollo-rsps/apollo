package org.apollo.game.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link SkillSet} class.
 *
 * @author munggs
 */
public final class SkillSetTests {

	/**
	 * Tests {@link SkillSet#addExperience(int, double)}.
	 */
	@Test
	public void addExperience() {
		SkillSet skillSet = new SkillSet();
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 1);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getExperience(), 0, 0);

		skillSet.addExperience(Skill.ATTACK, 5);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 1);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getExperience(), 5, 0);

		skillSet.addExperience(Skill.ATTACK, 150);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 2);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getExperience(), 155, 0);

		skillSet.addExperience(Skill.ATTACK, 200000000);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 99);
		assertEquals(skillSet.getSkill(Skill.ATTACK).getExperience(), 200000000, 0);
	}

	/**
	 * Tests {@link SkillSet#calculateCombatLevel()}.
	 */
	@Test
	public void calculateCombatLevel() {
		SkillSet skillSet = new SkillSet();
		assertEquals(skillSet.getCombatLevel(), 3);

		skillSet.addExperience(Skill.ATTACK, 200);
		skillSet.calculateCombatLevel();
		assertEquals(skillSet.getCombatLevel(), 4);

		skillSet.setMaximumLevel(Skill.MAGIC, 10);
		skillSet.calculateCombatLevel();
		assertEquals(skillSet.getCombatLevel(), 9);
	}

	/**
	 * Tests {@link SkillSet#normalize()}.
	 */
	@Test
	public void normalize() {
		SkillSet skillSet = new SkillSet();
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 1);
		skillSet.setMaximumLevel(Skill.ATTACK, 2);

		skillSet.normalize();
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 2);

		skillSet.normalize();
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 2);

		skillSet.setCurrentLevel(Skill.ATTACK, 3);
		skillSet.normalize();
		assertEquals(skillSet.getSkill(Skill.ATTACK).getCurrentLevel(), 2);
	}

}