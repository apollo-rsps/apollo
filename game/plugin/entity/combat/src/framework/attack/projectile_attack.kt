import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Mob


abstract class ProjectileAttack(
    speed: Int,
    range: Int,
    type: AttackType,
    style: AttackStyle,
    attackAnimation: Animation,
    requirements: MutableList<AttackRequirement>
) : BasicAttack(speed, range, type, style, attackAnimation, requirements) {

    abstract fun projectile(mob: Mob): ProjectileTemplate

    override fun doAttack(source: Mob, target: Mob, output: AttackOutput) {
        val projectileTemplate = projectile(source)
        val projectile = projectileTemplate.factory.invoke(source.world, source.position, target)

        projectileTemplate.castGraphic?.let { source.playGraphic(it) }
        source.world.spawn(projectile)
        output.projectile(projectile)
    }
}