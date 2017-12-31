import MobAttributeDelegators.attribute
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import java.util.*

//@todo - CombatState interface and NpcCombatState/PlayerCombatState to better drive
// behaviours
object CombatStateManager {
    private val states = WeakHashMap<Mob, CombatState>()

    fun stateFor(mob: Mob): CombatState {
        return states.computeIfAbsent(mob, {
            val combatStyle = when (mob) {
                is Player -> mob.weapon.weaponClass.styles[mob.combatStyle]
                is Npc -> Weapons[null].weaponClass.styles[0] // @todo
                else -> throw IllegalStateException("Invalid type: ${mob.javaClass.name}")
            }

            CombatState(it, combatStyle.attack)
        })
    }

    fun remove(it: Mob) {
        states.remove(it)
    }
}

var Mob.combatStyle: Int by attribute("combat_style", 0)
var Mob.combatAttackTick: Long by attribute("combat_attack_tick", 0)

class CombatState(private val mob: Mob, var attack: Attack) {
    var target: Mob? by WeakRefHolder()

    fun ticksSinceAttack(): Long {
        return mob.world.tick() - mob.combatAttackTick
    }

    fun inRange(): Boolean {
        val target = this.target ?: return false
        val distance = mob.position.getDistance(target.position)
        val objectType = when (attack.type) {
            AttackType.Ranged -> EntityType.PROJECTILE
            else -> EntityType.NPC
        }

        return distance <= attack.range /* && mob.world.collisionManager.raycast(mob.position, target.position, objectType) */
    }

    fun canAttack(): Boolean {
        return ticksSinceAttack() >= attack.speed
    }
}

val Mob.combatState: CombatState
    get() = CombatStateManager.stateFor(this)

