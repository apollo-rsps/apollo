package org.apollo.game.plugins.api

import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.SkillSet

val Mob.skills: SkillSet get() = skillSet
val SkillSet.attack: Skill get() = getSkill(Skill.ATTACK)
val SkillSet.defence: Skill get() = getSkill(Skill.DEFENCE)
val SkillSet.strength: Skill get() = getSkill(Skill.STRENGTH)
val SkillSet.hitpoints: Skill get() = getSkill(Skill.HITPOINTS)
val SkillSet.ranged: Skill get() = getSkill(Skill.RANGED)
val SkillSet.prayer: Skill get() = getSkill(Skill.PRAYER)
val SkillSet.magic: Skill get() = getSkill(Skill.MAGIC)
val SkillSet.cooking: Skill get() = getSkill(Skill.COOKING)
val SkillSet.woodcutting: Skill get() = getSkill(Skill.WOODCUTTING)
val SkillSet.fletching: Skill get() = getSkill(Skill.FLETCHING)
val SkillSet.fishing: Skill get() = getSkill(Skill.FISHING)
val SkillSet.firemaking: Skill get() = getSkill(Skill.FIREMAKING)
val SkillSet.crafting: Skill get() = getSkill(Skill.CRAFTING)
val SkillSet.smithing: Skill get() = getSkill(Skill.SMITHING)
val SkillSet.mining: Skill get() = getSkill(Skill.MINING)
val SkillSet.herblore: Skill get() = getSkill(Skill.HERBLORE)
val SkillSet.agility: Skill get() = getSkill(Skill.AGILITY)
val SkillSet.thieving: Skill get() = getSkill(Skill.THIEVING)
val SkillSet.slayer: Skill get() = getSkill(Skill.SLAYER)
val SkillSet.farming: Skill get() = getSkill(Skill.FARMING)
val SkillSet.runecraft: Skill get() = getSkill(Skill.RUNECRAFT)
