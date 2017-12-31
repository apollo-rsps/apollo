import AttackStyle.*
import AttackType.Ranged
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugins.api.attack
import org.apollo.game.plugins.api.defence
import org.apollo.game.plugins.api.skills


abstract class Attack(
    val speed: Int,
    val range: Int,
    val type: AttackType,
    val style: AttackStyle,
    private val attackAnimation: Animation,
    protected val requirements: MutableList<AttackRequirement>
) {
    /**
     * Calculate the maximum damage this [Attack] can give out for the [source] [Mob].
     */
    abstract fun maxDamage(source: Mob): Int

    /**
     * Check the requirements for this attack and execute it, making [AttackHitAttempt] rolls
     * and depleting any resources used by [AttackRequirement]s.
     */
    fun execute(source: Mob, target: Mob) {
        val failingRequirement: AttackRequirementResult? = requirements.map({ it.check(source) })
            .firstOrNull({ it != AttackRequirementResult.Ok })

        when (failingRequirement) {
            is AttackRequirementResult.Failed -> {
                (source as? Player)?.sendMessage(failingRequirement.message)
                return
            }
        }

        // @todo - refactor this out somewhere, all rolls are the same calculations
        // (damage, hit + defence)

        val effectiveAttackBase = source.skills.attack.currentLevel

        //@todo - attack prayers
        val effectiveAttackModifier = 1.0
        val attackStyleBonus = when (style) {
            Accurate -> 3
            Controlled -> 1
            LongRanged -> 1
            else -> 0
        }

        val effectiveAttack = effectiveAttackBase * effectiveAttackModifier + attackStyleBonus + 8
        //@todo - get attack bonus from stats
        val attackEquipmentBonus = 0
        val maxHitRoll = effectiveAttack * (attackEquipmentBonus + 64)

        val effectiveDefenceBase = target.skills.defence.currentLevel

        //@todo - defence prayers
        val effectiveDefenceModifier = 1.0
        val defenceStyleBonus = when (style) {
            Defensive, LongRanged -> 3
            Controlled -> 1
            else -> 0
        }

        val effectiveDefence = effectiveDefenceBase * effectiveDefenceModifier + defenceStyleBonus + 8
        //@todo - get defence bonus from stats
        val defenceEquipmentBonus = 0
        val maxDefenceRoll = effectiveDefence * (defenceEquipmentBonus + 64)

        val accuracy = if (maxHitRoll > maxDefenceRoll) {
            1 - (maxDefenceRoll + 2) / (2 * (maxHitRoll + 1))
        } else {
            maxHitRoll / (2 * (maxDefenceRoll + 1))
        }

        val maxDamageRoll = maxDamage(source)
        val damageOutput = AttackOutput(type)
        doAttack(source, target, damageOutput)

        requirements.forEach { it.apply(source) }
        source.playAnimation(attackAnimation)

        damageOutput.rolls.forEach {
            val hitRoll = Math.random()
            val hitDamage = if (accuracy >= hitRoll) {
                scale(0.0 to 0.99, 1.0 to (maxDamageRoll * it.modifier), it.roll)
            } else {
                0
            }

            target.damage(hitDamage, if (hitDamage == 0) 0 else 1, false)
        }
    }

    protected abstract fun doAttack(source: Mob, target: Mob, output: AttackOutput)
}

/**
 * A basic attack that calculates maximum damage using the <code>effective strength</code> calculation.
 *
 * @todo - only use attacks for damage output / playing effects. tracking speed/range/type/style can happen elsewhere.
 */
abstract class BasicAttack(
    speed: Int,
    range: Int,
    type: AttackType,
    style: AttackStyle,
    attackAnimation: Animation,
    requirements: MutableList<AttackRequirement>
) : Attack(speed, range, type, style, attackAnimation, requirements) {
    override fun maxDamage(source: Mob): Int {

        val effectiveStrengthBase = when (type) {
            Ranged -> source.skillSet.getCurrentLevel(Skill.RANGED)
            else -> source.skillSet.getCurrentLevel(Skill.STRENGTH)
        }

        // @todo - prayer + others (?)
        val effectiveStrengthModifier = 1.0

        val hitStyleBonus = when (style) {
            Aggressive -> 3
            LongRanged, Controlled -> 1
            Defensive -> 0
            Accurate -> if (type == Ranged) 3 else 0
            else -> 0
        }

        val effectiveStrength = Math.floor(effectiveStrengthBase * effectiveStrengthModifier) + hitStyleBonus + 8
        //@todo - get from combat bonuses
        val strengthBonus = 0

        val baseDamage = 0.5 + effectiveStrength * (strengthBonus + 64) / 640

        return Math.floor(baseDamage).toInt()
    }
}
