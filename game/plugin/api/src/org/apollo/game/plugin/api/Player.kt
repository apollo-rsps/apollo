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
class SkillProxy(private val skills: SkillSet, private val skill: Int) {

    /**
     * The maximum level of this skill.
     */
    val maximum: Int
        get() = skills.getMaximumLevel(skill)

    /**
     * The current level of this skill.
     */
    val current: Int
        get() = skills.getCurrentLevel(skill)

    /**
     * The amount of experience in this skill a player has.
     */
    var experience: Double
        get() = skills.getExperience(skill)
        set(value) {
            skills.setExperience(skill, value)
        }

    /**
     * Boosts the current level of this skill by [amount], if possible.
     */
    fun boost(amount: Int) {
        require(amount >= 1) { "Can only boost skills by positive values." }

        val new = if (current - maximum > amount) {
            current
        } else {
            Math.min(current + amount, maximum + amount)
        }

        skills.setCurrentLevel(skill, new)
    }

    /**
     * Drains the current level of this skill by [amount], if possible.
     */
    fun drain(amount: Int) {
        require(amount >= 1) { "Can only drain skills by positive values." }

        val new = Math.max(current - amount, 0)
        skills.setCurrentLevel(skill, new)
    }

    /**
     * Restores the current level of this skill by [amount], if possible.
     */
    fun restore(amount: Int) {
        require(amount >= 1) { "Can only restore skills by positive values." }

        val new = Math.min(current + amount, maximum)
        skills.setCurrentLevel(skill, new)
    }
}