
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.net.message.Message
import java.util.Objects

class MiningAction(
    player: Player,
    private val tool: Pickaxe?,
    private val target: MiningTarget
) : AsyncDistancedAction<Player>(PULSES, true, player, target.position, target.ore.objectSize) {

    companion object {
        private const val PULSES = 0

        /**
         * Starts a [MiningAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, ore: Ore) {
            val pickaxe = Pickaxe.bestFor(player)

            val target = MiningTarget(message.id, message.position, ore)
            val action = MiningAction(player, pickaxe, target)

            player.startAction(action)

            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        mob.turnTo(position)

        if (tool == null) {
            mob.sendMessage("You do not have a pickaxe for which you have the level to use.")
            stop()
        }

        if (!target.skillRequirementsMet(mob)) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
        }

        mob.sendMessage("You swing your pick at the rock.")

        while (isRunning) {
            mob.playAnimation(tool.animation)

            wait(tool.pulses)

            if (!target.isValid(mob.world)) {
                stop()
            }

            val successChance = rand(100)

            if (target.isSuccessful(mob, successChance)) {
                if (mob.inventory.freeSlots() == 0) {
                    mob.inventory.forceCapacityExceeded()
                    stop()
                }

                if (target.reward(mob)) {
                    mob.sendMessage("You manage to mine some ${target.oreName(mob)}")

                    if (target.ore.respawn != -1) {
                        target.deplete(mob.world)
                        stop()
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MiningAction
        return mob == other.mob && target == other.target
    }

    override fun hashCode(): Int = Objects.hash(mob, target)
}
