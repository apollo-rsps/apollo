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
                mob.sendMessage("You manage to mine some ${target.oreName()}")
                target.deplete(mob.world)

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
     * Get the [GameObject] represented by this target.
     *
     * @todo: api: shouldn't be as verbose
     */
    private fun getObject(world: World): GameObject? {
        val region = world.regionRepository.fromPosition(position)
        return region.findObject(position, objectId).orElse(null)
    }

    /**
     * Deplete this mining resource from the [World], and schedule it to be respawned
     * in a number of ticks specified by the [Ore].
     */
    fun deplete(world: World) {
        world.expireObject(getObject(world)!!, ore.objects[objectId]!!, ore.respawn)
    }

    /**
     * Check if the [Player] was successful in mining this ore with a random success [chance] value between 0 and 100.
     */
    fun isSuccessful(mob: Player, chance: Int): Boolean {
        val percent = (ore.chance * mob.mining.current + ore.chanceOffset) * 100
        return chance < percent
    }

    /**
     * Check if this target is still valid in the [World] (i.e. has not been [deplete]d).
     */
    fun isValid(world: World) = getObject(world) != null

    /**
     * Get the normalized name of the [Ore] represented by this target.
     */
    fun oreName() = Definitions.item(ore.id)!!.name.toLowerCase()

    /**
     * Reward a [player] with experience and ore if they have the inventory capacity to take a new ore.
     */
    fun reward(player: Player): Boolean {
        val hasInventorySpace = player.inventory.add(ore.id)

        if (hasInventorySpace) {
            player.mining.experience += ore.exp
        }

        return hasInventorySpace
    }

    /**
     * Check if the [mob] has met the skill requirements to mine te [Ore] represented by
     * this [MiningTarget].
     */
    fun skillRequirementsMet(mob: Player) = mob.mining.current < ore.level
}

