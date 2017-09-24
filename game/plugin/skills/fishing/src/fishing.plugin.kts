import Fishing_plugin.FishingAction
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.fishing.FishingSpot
import org.apollo.game.plugin.skills.fishing.FishingTool
import org.apollo.game.plugins.api.fishing
import org.apollo.game.plugins.api.skills
import java.util.Objects
import java.util.Random

// TODO: moving fishing spots, seaweed and caskets, evil bob

class FishingAction(player: Player, position: Position, val option: FishingSpot.Option) :
    AsyncDistancedAction<Player>(0, true, player, position, SPOT_DISTANCE) {

    companion object {
        private const val SPOT_DISTANCE = 1
        private const val FISHING_DELAY = 4

        /**
         * The random number generator used by the fishing plugin.
         */
        private val random = Random()

        /**
         * Returns whether or not the catch was successful.
         * TODO: We need to identify the correct algorithm for this
         */
        private fun successfulCatch(level: Int, req: Int): Boolean = minOf(level - req + 5, 40) > random.nextInt(100)

        /**
         * Returns whether or not the [Player] has (or does not need) bait.
         */
        private fun hasBait(player: Player, bait: Int): Boolean = bait == -1 || player.inventory.contains(bait)

        /**
         * @return if the player has the needed tool to fish at the spot.
         */
        private fun hasTool(player: Player, tool: FishingTool): Boolean = player.equipment.contains(tool.id) ||
            player.inventory.contains(tool.id)

    }

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

            val level = mob.skills.fishing.currentLevel
            val fish = option.sample(level)

            if (successfulCatch(level, fish.level)) {
                if (tool.bait != -1) {
                    mob.inventory.remove(tool.bait)
                }

                mob.inventory.add(fish.id)
                mob.sendMessage("You catch a ${fish.formattedName}.")
                mob.skills.addExperience(Skill.FISHING, fish.experience)

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
        if (mob.skills.fishing.currentLevel < option.level) {
            mob.sendMessage("You need a fishing level of ${option.level} to fish at this spot.")
            return false
        } else if (!hasTool(mob, tool)) {
            mob.sendMessage("You need a ${tool.formattedName} to fish at this spot.")
            return false
        } else if (!hasBait(mob, tool.bait)) {
            mob.sendMessage("You need some ${tool.baitName} to fish at this spot.")
            return false
        } else if (mob.inventory.freeSlots() == 0) {
            mob.inventory.forceCapacityExceeded()
            return false
        }

        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FishingAction

        return option == other.option && position == other.position && mob == other.mob
    }

    override fun hashCode(): Int = Objects.hash(option, position, mob)

}

/**
 * Intercepts the [NpcActionMessage] and starts a [FishingAction] if the npc
 */
on { NpcActionMessage::class }
    .where { option == 1 || option == 3 }
    .then {
        val entity = it.world.npcRepository[index]
        val spot = FishingSpot.lookup(entity.id) ?: return@then

        val option = spot.option(option)
        it.startAction(FishingAction(it, entity.position, option))

        terminate()
    }
