import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player


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

        val maxHitRoll = calculateBasicMaxRoll(RollType.Attack, source)
        val maxDefenceRoll = calculateBasicMaxRoll(RollType.Defence, target)

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
        return calculateBasicMaxRoll(RollType.Damage, source)
    }
}
