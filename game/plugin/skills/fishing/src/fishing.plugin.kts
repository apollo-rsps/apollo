
import Fishing_plugin.FishingAction
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.fishing
import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.fishing.FishingSpot
import org.apollo.game.plugin.skills.fishing.FishingTool
import java.util.Objects

// TODO: moving fishing spots, seaweed and caskets, evil bob

/**
 * Intercepts the [NpcActionMessage] and starts a [FishingAction] if the npc
 */
on { NpcActionMessage::class }
    .where { option == 1 || option == 3 }
    .then { player ->
        val entity = player.world.npcRepository[index]
        val spot = FishingSpot.lookup(entity.id) ?: return@then

        val option = spot.option(option)
        player.startAction(FishingAction(player, entity.position, option))

        terminate()
    }

class FishingAction(player: Player, position: Position, val option: FishingSpot.Option) :
    AsyncDistancedAction<Player>(0, true, player, position, SPOT_DISTANCE) {

    /**
     * The [FishingTool] used for the fishing spot.
     */
    private val tool = option.tool

    override fun action(): ActionBlock = {
        if (!verify()) {
            stop()
        }

        mob.turnTo(position)
        mob.sendMessage(tool.message)

        while (isRunning) {
            mob.playAnimation(tool.animation)
            wait(FISHING_DELAY)

            val level = mob.fishing.current
            val fish = option.sample(level)

            if (successfulCatch(level, fish.level)) {
                if (tool.bait != -1) {
                    mob.inventory.remove(tool.bait)
                }

                mob.inventory.add(fish.id)
                mob.sendMessage(fish.catchMessage)
                mob.fishing.experience += fish.experience

                if (mob.inventory.freeSlots() == 0) {
                    mob.inventory.forceCapacityExceeded()

                    mob.stopAnimation()
                    stop()
                } else if (!hasBait(mob, tool.bait)) {
                    mob.sendMessage("You need more ${tool.baitName} to fish at this spot.")

                    mob.stopAnimation()
                    stop()
                }
            }
        }
    }

    /**
     * Verifies that the player can gather fish from the [FishingSpot] they clicked.
     */
    private fun verify(): Boolean {
        val current = mob.fishing.current

        when {
            current < option.level -> mob.sendMessage("You need a fishing level of ${option.level} to fish at this spot.")
            !hasTool(mob, tool) -> mob.sendMessage("You need a ${tool.formattedName} to fish at this spot.")
            !hasBait(mob, tool.bait) -> mob.sendMessage("You need some ${tool.baitName} to fish at this spot.")
            mob.inventory.freeSlots() == 0 -> mob.inventory.forceCapacityExceeded()
            else -> return true
        }

        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FishingAction
        return option == other.option && position == other.position && mob == other.mob
    }

    override fun hashCode(): Int = Objects.hash(option, position, mob)

    private companion object {
        private const val SPOT_DISTANCE = 1
        private const val FISHING_DELAY = 4

        /**
         * Returns whether or not the catch was successful.
         * TODO: We need to identify the correct algorithm for this
         */
        private fun successfulCatch(level: Int, req: Int): Boolean = minOf(level - req + 5, 40) > rand(100)

        /**
         * Returns whether or not the [Player] has (or does not need) bait.
         */
        private fun hasBait(player: Player, bait: Int): Boolean = bait == -1 || player.inventory.contains(bait)

        /**
         * Returns whether or not the player has the required tool to fish at the spot.
         */
        private fun hasTool(player: Player, tool: FishingTool): Boolean = player.equipment.contains(tool.id) ||
            player.inventory.contains(tool.id)

    }

}