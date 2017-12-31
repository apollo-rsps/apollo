import org.apollo.game.model.Animation
import org.apollo.game.model.entity.EquipmentConstants
import org.apollo.game.model.entity.Mob

class RangeAttackFactory(val ammoType: AmmoType) : AttackFactory {
    override fun createAttack(speed: Int, range: Int, type: AttackType, style: AttackStyle, animation: Animation): Attack {
        return RangeAttack(speed, range, type, style, animation, ammoType)
    }
}

class RangeAttack(
    speed: Int,
    range: Int,
    type: AttackType,
    style: AttackStyle,
    attackAnimation: Animation,
    private val ammoType: AmmoType
) : ProjectileAttack(speed, range, type, style, attackAnimation, mutableListOf()) {

    init {
        requirements.add(AmmoRequirement(ammoType, 1))
    }

    private fun ammo(mob: Mob): Ammo {
        return mob.equipment[EquipmentConstants.ARROWS]?.let { ammoType.items[it.id] }
            ?: throw IllegalStateException("Couldn't find ammo entry for equipped item")
    }

    override fun projectile(mob: Mob) = ammo(mob).let { ProjectileTemplate(it.projectileFactory, it.attack) }
}