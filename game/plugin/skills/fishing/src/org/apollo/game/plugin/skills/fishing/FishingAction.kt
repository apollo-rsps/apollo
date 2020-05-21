package org.apollo.game.plugin.skills.fishing

import java.util.Objects
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.fishing

class FishingAction(
    player: Player,
    private val target: FishingTarget
) : AsyncDistancedAction<Player>(0, true, player, target.position, SPOT_DISTANCE) {

    /**
     * The [FishingTool] used for the fishing spot.
     */
    private val tool = target.option.tool

    override fun action(): ActionBlock = {
        if (!target.verify(mob)) {
            stop()
        }

        mob.turnTo(position)
        mob.sendMessage(tool.message)

        while (isRunning) {
            mob.playAnimation(tool.animation)
            wait(FISHING_DELAY)

            val level = mob.fishing.current
            val fish = target.option.sample(level)

            if (target.isSuccessful(mob, fish.level)) {
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

    override fun equals(other: Any?): Boolean {
        if (other is FishingAction) {
            return position == other.position && target == other.target && mob == other.mob
        }

        return false
    }

    override fun hashCode(): Int = Objects.hash(target, position, mob)

    internal companion object {
        private const val SPOT_DISTANCE = 1
        private const val FISHING_DELAY = 4

        /**
         * Returns whether or not the [Player] has (or does not need) bait.
         */
        internal fun hasBait(player: Player, bait: Int): Boolean {
            return bait == -1 || bait in player.inventory
        }

        /**
         * Returns whether or not the player has the required tool to fish at the spot.
         */
        internal fun hasTool(player: Player, tool: FishingTool): Boolean {
            return tool.id in player.equipment || tool.id in player.inventory
        }
    }
}