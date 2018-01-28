import AttackType.Magic
import AttackType.Ranged
import CombatBonus.MeleeStrength
import CombatBonus.RangedStrength
import org.apollo.game.model.entity.Mob
import org.apollo.game.plugins.api.*

enum class RollType {
    Attack,
    Defence,
    Damage
}

fun calculateBasicMaxRoll(rollType: RollType, mob: Mob, modifiers: List<Double> = emptyList()): Int {
    val style = mob.combatState.attack.style
    val attackType = mob.combatState.attack.type

    if (attackType == Magic) {
        throw IllegalStateException("Basic roll calculator called for a magic attack.  Unsupported")
    }
    
    val styleBonus = when (rollType) {
        RollType.Attack -> style.attackBonus
        RollType.Defence -> style.defenceBonus
        RollType.Damage -> {
            if (style == AttackStyle.Accurate && attackType != Ranged) {
                0
            } else {
                style.strengthBonus
            }
        }
    }

    val baseSkill = when (rollType) {
        RollType.Attack -> {
            if (attackType == Ranged) {
                mob.skills.ranged
            } else {
                mob.skills.attack
            }
        }
        RollType.Defence -> mob.skills.defence
        RollType.Damage -> {
            if (attackType == Ranged) {
                mob.skills.ranged
            } else {
                mob.skills.strength
            }
        }
    }

    val equipmentBonuses = mob.combatState.bonuses
    val equipmentBonus = when (rollType) {
        RollType.Attack -> equipmentBonuses.attack[attackType]
        RollType.Defence -> equipmentBonuses.defence[attackType]
        RollType.Damage -> {
            if (attackType == Ranged) {
                equipmentBonuses[RangedStrength]
            } else {
                equipmentBonuses[MeleeStrength]
            }
        }
    }

    val modifier = modifiers.reduce { a, b -> a + b }
    val effectiveLevel = baseSkill.currentLevel * modifier + styleBonus + 8
    val maxRoll = if (rollType == RollType.Damage) {
        0.5 + effectiveLevel * (equipmentBonus + 64) / 640
    } else {
        effectiveLevel * (equipmentBonus + 64)
    }

    return maxRoll.toInt()
}