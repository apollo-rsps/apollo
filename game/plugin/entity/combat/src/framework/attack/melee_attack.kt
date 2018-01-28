import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Mob

object MeleeAttackFactory : AttackFactory {
    override fun createAttack(speed: Int, range: Int, type: AttackType, style: AttackStyle, animation: Animation): Attack {
        return MeleeAttack(speed, range, type, style, animation, mutableListOf())
    }
}

class MeleeAttack(
    speed: Int,
    range: Int,
    type: AttackType,
    style: AttackStyle,
    attackAnimation: Animation,
    requirements: MutableList<AttackRequirement>
) : BasicAttack(speed, range, type, style, attackAnimation, requirements) {

    override fun doAttack(source: Mob, target: Mob, output: AttackOutput) = output.hit()

}