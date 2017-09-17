import org.apollo.cache.def.ItemDefinition
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.skills.woodcutting.*
import org.apollo.game.plugins.api.Definitions
import org.apollo.game.plugins.api.woodcutting
import org.apollo.game.plugins.api.skills
import org.apollo.game.scheduling.ScheduledTask
import java.util.*

class WoodcuttingTarget(val objectId: Int, val position: Position, val wood: Wood) {

    fun getObject(world: World): Optional<GameObject> {
        val region = world.regionRepository.fromPosition(position)
        val obj = region.findObject(position, objectId)

        return obj
    }

    fun isSuccessful(mob: Player): Boolean {
        return rand(100) <= wood.chance * 100
    }
}

class WoodcuttingAction(val player: Player, val tool: Axe, val target: WoodcuttingTarget) : AsyncDistancedAction<Player>(PULSES, true, player, target.position, TREE_SIZE) {

    companion object {
        private val PULSES = 0
        private val TREE_SIZE = 1;

        fun axeFor(player: Player): Axe? {
            return AXES
                    .filter { it.level <= player.skills.woodcutting.currentLevel }
                    .filter { player.equipment.contains(it.id) || player.inventory.contains(it.id) }
                    .sortedByDescending { it.level }
                    .firstOrNull()
        }

        /**
         * Starts a [WoodcuttingAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, wood: Wood) {
            val axe = axeFor(player)
            if (axe != null) {
                val action = WoodcuttingAction(player, axe, WoodcuttingTarget(message.id, message.position, wood))
                player.startAction(action)
            } else {
                player.sendMessage("You do not have a axe for which you have the level to use.")
            }

            message.terminate()
        }
    }



    override fun start() {
        mob.turnTo(position)

        val level = mob.skills.woodcutting.currentLevel

        if (level < target.wood.level) {
            mob.sendMessage("You do not have the required level to cut down this tree.")
            stop()
        }
    }

    override suspend fun executeActionAsync() {
        mob.sendMessage("You swing your axe at the tree.")
        mob.playAnimation(tool.animation)

        wait(tool.pulses)
        //

        val obj = target.getObject(mob.world)
        if (!obj.isPresent) {
            stop()
            return
        }

        if (mob.inventory.freeSlots() == 0) {
            mob.inventory.forceCapacityExceeded()
            stop()
            return
        }
        if (mob.inventory.add(target.wood.id)) {
            //TODO: Use lookup from utils once it has a lookup function for IDs
            val woodName = ItemDefinition.lookup(target.wood.id).name.toLowerCase();
            mob.sendMessage("You managed to cut some $woodName.")
            mob.skillSet.addExperience(Skill.WOODCUTTING, target.wood.exp)
        }

        if (target.isSuccessful(mob)) {
            //respawn time: http://runescape.wikia.com/wiki/Trees
            val respawn = ((30 * 1000) / 600) + ((rand(150) * 1000) / 600) // between 30 sec and 3 min respawm
            mob.world.expireObject(obj.get(), target.wood.stump, respawn)
        }
    }
}

on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val wood = lookupTree(id)
            if (wood != null) {
                WoodcuttingAction.start(this, it, wood)
            }
        }
