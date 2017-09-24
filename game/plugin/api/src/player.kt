package org.apollo.game.plugin.api

import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.SkillSet

val Player.attack: SkillProxy get() = SkillProxy(skillSet, Skill.ATTACK)
val Player.defence: SkillProxy get() = SkillProxy(skillSet, Skill.DEFENCE)
val Player.strength: SkillProxy get() = SkillProxy(skillSet, Skill.STRENGTH)
val Player.hitpoints: SkillProxy get() = SkillProxy(skillSet, Skill.HITPOINTS)
val Player.ranged: SkillProxy get() = SkillProxy(skillSet, Skill.RANGED)
val Player.prayer: SkillProxy get() = SkillProxy(skillSet, Skill.PRAYER)
val Player.magic: SkillProxy get() = SkillProxy(skillSet, Skill.MAGIC)
val Player.cooking: SkillProxy get() = SkillProxy(skillSet, Skill.COOKING)
val Player.woodcutting: SkillProxy get() = SkillProxy(skillSet, Skill.WOODCUTTING)
val Player.fletching: SkillProxy get() = SkillProxy(skillSet, Skill.FLETCHING)
val Player.fishing: SkillProxy get() = SkillProxy(skillSet, Skill.FISHING)
val Player.firemaking: SkillProxy get() = SkillProxy(skillSet, Skill.FIREMAKING)
val Player.crafting: SkillProxy get() = SkillProxy(skillSet, Skill.CRAFTING)
val Player.smithing: SkillProxy get() = SkillProxy(skillSet, Skill.SMITHING)
val Player.mining: SkillProxy get() = SkillProxy(skillSet, Skill.MINING)
val Player.herblore: SkillProxy get() = SkillProxy(skillSet, Skill.HERBLORE)
val Player.agility: SkillProxy get() = SkillProxy(skillSet, Skill.AGILITY)
val Player.thieving: SkillProxy get() = SkillProxy(skillSet, Skill.THIEVING)
val Player.slayer: SkillProxy get() = SkillProxy(skillSet, Skill.SLAYER)
val Player.farming: SkillProxy get() = SkillProxy(skillSet, Skill.FARMING)
val Player.runecraft: SkillProxy get() = SkillProxy(skillSet, Skill.RUNECRAFT)

/**
 * A proxy class to allow
 */
class SkillProxy(val skills: SkillSet, val skill: Int) {

    /**
     * The maximum level of this skill.
     */
    val maximum = skills.getMaximumLevel(skill)

    /**
     * The current level of this skill.
     */
    val current = skills.getCurrentLevel(skill)

    val experience = ExperienceProxy()

    /**
     * A proxy class to make [experience] (effectively) write-only.
     */
    inner class ExperienceProxy {

        operator fun plusAssign(amount: Int) = skills.addExperience(skill, amount.toDouble())

        operator fun plusAssign(amount: Double) = skills.addExperience(skill, amount)

    }

    /**
     * Boosts the current level of this skill by [amount], if possible (i.e. if `current + amount <= maximum + amount`).
     */
    fun boost(amount: Int) {
        val new = Math.min(current + amount, maximum + amount)
        skills.setCurrentLevel(skill, new)
    }

    /**
     * Drains the current level of this skill by [amount], if possible (i.e. if `current - amount >= 0`).
     */
    fun drain(amount: Int) {
        val new = Math.max(current - amount, 0)
        skills.setCurrentLevel(skill, new)
    }

    /**
     * Restores the current level of this skill by [amount], if possible (i.e. if `current + amount < maximum`).
     */
    fun restore(amount: Int) {
        val new = Math.max(current + amount, maximum)
        skills.setCurrentLevel(skill, new)
    }

}