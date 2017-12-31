import org.apollo.game.action.Action
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player
import org.apollo.net.message.Message

class CombatAction<T : Mob>(mob: T) : Action<T>(0, true, mob) {
    companion object {

        /**
         * Starts a [CombatAction] for the specified [Player], terminating the [Message] that triggered.
         */
        fun start(message: Message, player: Player, target: Mob) {
            player.combatState.target = target
            player.interactingMob = target
            player.turnTo(target.position)
            player.startAction(CombatAction(player))
            message.terminate()
        }
    }

    override fun execute() {
        val state = mob.combatState
        val target = state.target
        val attack = state.attack

        if (target == null) {
            stop()
            return
        }

        if (!state.inRange()) {
            // @todo - chase 'til in range
            return
        } else {
            // @todo - chasing will prevent running closer than needed
            mob.walkingQueue.clear()
        }

        if (!state.canAttack()) {
            // @todo - idle - waiting to attack, do block animation
            return
        }

        attack.execute(mob, target)
        mob.combatAttackTick = mob.world.tick()
    }
}