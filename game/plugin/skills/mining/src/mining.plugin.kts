import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.api.expireObject
import org.apollo.game.plugin.api.findObject
import org.apollo.game.plugin.api.mining
import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.net.message.Message
import java.util.Objects

on { ObjectActionMessage::class }
    .where { option == Actions.MINING }
    .then { player ->
        Ore.fromRock(id)?.let { ore ->
            MiningAction.start(this, player, ore)
        }
    }

on { ObjectActionMessage::class }
    .where { option == Actions.PROSPECTING }
    .then { player ->
        val ore = Ore.fromRock(id)

        if (ore != null) {
            ProspectingAction.start(this, player, ore)
        } else if (Ore.fromExpiredRock(id) != null) {
            ExpiredProspectingAction.start(this, player)
        }
    }

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

        val level = mob.mining.current
        if (level < target.ore.level) {
            mob.sendMessage("You do not have the required level to mine this rock.")
            stop()
        }

        mob.sendMessage("You swing your pick at the rock.")
        mob.playAnimation(tool.animation)

        wait(tool.pulses)

        val obj = target.getObject(mob.world) ?: stop()

        if (target.isSuccessful(mob)) {
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

    fun getObject(world: World): GameObject? {
        val region = world.regionRepository.fromPosition(position)
        return region.findObject(position, objectId).orElse(null)
    }

    fun isSuccessful(mob: Player): Boolean {
        val percent = (ore.chance * mob.mining.current + ore.chanceOffset) * 100
        return rand(100) < percent
    }

}

private object Actions {
    const val MINING = 1
    const val PROSPECTING = 2
}