
import java.util.concurrent.TimeUnit
import org.apollo.game.GameConstants
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.api.*
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.skills.woodcutting.Tree

// TODO Accurate chopping rates, e.g. https://twitter.com/JagexKieren/status/713403124464107520

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then { player ->
        Tree.lookup(id)?.let { WoodcuttingAction.start(this, player, it) }
    }

class WoodcuttingTarget(private val objectId: Int, val position: Position, val tree: Tree) {

    /**
     * Get the tree object in the world
     */
    fun getObject(world: World): GameObject? {
        return world.findObject(position, objectId)
    }

    /**
     * Returns whether or not the tree was cut down.
     */
    fun isCutDown(): Boolean = rand(100) <= tree.chance * 100
}

class WoodcuttingAction(
    player: Player,
    private val tool: Axe,
    private val target: WoodcuttingTarget
) : AsyncDistancedAction<Player>(DELAY, true, player, target.position, TREE_SIZE) {

    companion object {
        private const val DELAY = 0
        private const val TREE_SIZE = 2
        private const val MINIMUM_RESPAWN_TIME = 30L // In seconds

        /**
         * Starts a [WoodcuttingAction] for the specified [Player], terminating the [ObjectActionMessage] that triggered
         * it.
         */
        fun start(message: ObjectActionMessage, player: Player, wood: Tree) {
            val axe = Axe.bestFor(player)
            if (axe != null) {
                if (player.inventory.freeSlots() == 0) {
                    player.inventory.forceCapacityExceeded()
                    return
                }

                val action = WoodcuttingAction(player, axe, WoodcuttingTarget(message.id, message.position, wood))
                player.startAction(action)
            } else {
                player.sendMessage("You do not have an axe for which you have the level to use.")
            }

            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        mob.turnTo(position)

        val level = mob.woodcutting.current
        if (level < target.tree.level) {
            mob.sendMessage("You do not have the required level to cut down this tree.")
            stop()
        }

        while (isRunning) {
            mob.sendMessage("You swing your axe at the tree.")
            mob.playAnimation(tool.animation)

            wait(tool.pulses)

            // Check that the object exists in the world
            val obj = target.getObject(mob.world) ?: stop()

            if (mob.inventory.add(target.tree.id)) {
                val logName = Definitions.item(target.tree.id).name.toLowerCase()
                mob.sendMessage("You managed to cut some $logName.")
                mob.woodcutting.experience += target.tree.exp
            }

            if (target.isCutDown()) {
                // respawn time: http://runescape.wikia.com/wiki/Trees
                val respawn = TimeUnit.SECONDS.toMillis(MINIMUM_RESPAWN_TIME + rand(150)) / GameConstants.PULSE_DELAY

                mob.world.replaceObject(obj, target.tree.stump, respawn.toInt())
                stop()
            }
        }
    }
}