package org.apollo.game.plugin.skills.fishing

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.fishing
import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.fishing.FishingAction.Companion.hasBait
import org.apollo.game.plugin.skills.fishing.FishingAction.Companion.hasTool

data class FishingTarget(val position: Position, val option: FishingSpot.Option) {

    /**
     * Returns whether or not the catch was successful.
     * TODO: We need to identify the correct algorithm for this
     */
    fun isSuccessful(player: Player, req: Int): Boolean {
        return minOf(player.fishing.current - req + 5, 40) > rand(100)
    }

    /**
     * Verifies that the [Player] can gather fish from their chosen [FishingSpot.Option].
     */
    fun verify(player: Player): Boolean {
        val current = player.fishing.current
        val required = option.level
        val tool = option.tool

        when {
            current < required -> player.sendMessage("You need a fishing level of $required to fish at this spot.")
            hasTool(player, tool) -> player.sendMessage("You need a ${tool.formattedName} to fish at this spot.")
            hasBait(player, tool.bait) -> player.sendMessage("You need some ${tool.baitName} to fish at this spot.")
            player.inventory.freeSlots() == 0 -> player.inventory.forceCapacityExceeded()
            else -> return true
        }

        return false
    }
}