import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.api.*
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.net.message.Message
import java.util.*

class MiningAction(
    player: Player,
    private val tool: Pickaxe,
    private val target: MiningTarget
) : AsyncDistancedAction<Player>(PULSES, true, player, target.position, ORE_SIZE) {

    companion object {
        private const val PULSES = 0
        private const val ORE_SIZE = 1

        /**
         * Starts a [MiningAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, ore: Ore) {
            val pickaxe = Pickaxe.bestFor(player)

            if (pickaxe == null) {
                player.sendMessage("You do not have a pickaxe for which you have the level to use.")
            } else {
                val target = MiningTarget(message.id, message.position, ore)
                val action = MiningAction(player, pickaxe, target)

                player.startAction(action)
            }

            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        mob.turnTo(position)

        if (!target.skillRequirementsMet(mob)) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
        }

        mob.sendMessage("You swing your pick at the rock.")
        mob.playAnimation(tool.animation)

        wait(tool.pulses)

        val obj = target.getObject(mob.world) ?: stop()
        val successScore = rand(100)

        if (target.isSuccessful(mob, successScore)) {
            if (mob.inventory.freeSlots() == 0) {
                mob.inventory.forceCapacityExceeded()
                stop()
            }

            if (mob.inventory.add(target.ore.id)) {
                val oreName = Definitions.item(target.ore.id)!!.name.toLowerCase()
                mob.sendMessage("You manage to mine some $oreName")

                mob.mining.experience += target.ore.exp
                mob.world.expireObject(obj, target.ore.objects[target.objectId]!!, target.ore.respawn)
                stop()
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

data class MiningTarget(val objectId: Int, val position: Position, val ore: Ore) {

    /**
     * Check if the [mob] has met the skill requirements to mine te [Ore] represented by
     * this [MiningTarget].
     */
    fun skillRequirementsMet(mob: Player): Boolean {
        return mob.mining.current < ore.level
    }

    fun getObject(world: World): GameObject? {
        val region = world.regionRepository.fromPosition(position)
        return region.findObject(position, objectId).orElse(null)
    }

    fun isSuccessful(mob: Player, score: Int): Boolean {
        val percent = (ore.chance * mob.mining.current + ore.chanceOffset) * 100
        return score < percent
    }

}

