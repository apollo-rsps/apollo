import org.apollo.game.model.entity.Projectile

data class AttackHitAttempt(val delay: Int, val roll: Double, val modifier: Double) {
    fun isImmediate(): Boolean {
        return delay == 0
    }
}

class AttackOutput(val type: AttackType) {
    val rolls = mutableListOf<AttackHitAttempt>()

    fun hit(modifier: Double = 1.0) {
        rolls.add(AttackHitAttempt(0, Math.random(), modifier))
    }

    fun projectile(projectile: Projectile, modifier: Double = 1.0) {
        val source = projectile.position
        val target = projectile.destination
        val projectileLifetime = projectile.delay + projectile.lifetime + source.getDistance(target) * 5
        val projectileTicks = Math.floor(projectileLifetime * 0.02587)

        rolls.add(AttackHitAttempt(projectileTicks.toInt(), Math.random(), modifier))
    }
}