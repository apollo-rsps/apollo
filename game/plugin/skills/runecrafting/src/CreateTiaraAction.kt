package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.runecraft

class CreateTiaraAction(val player: Player, val position: Position, val tiara: Tiara, val altar: Altar) : DistancedAction<Player>(0, true, player, position, 2) {
    override fun executeAction() {
        if (tiara.altar != altar) {
            player.sendMessage("You can't use that talisman on this altar.")
            stop()
            return
        }

        if (player.inventory.contains(blankTiaraId)) {
            player.inventory.remove(blankTiaraId)
            player.inventory.add(tiara.id)
            player.runecraft.experience += tiara.xp
            player.playAnimation(runecraftingAnimation)
            player.playGraphic(runecraftingGraphic)
            stop()
        }
    }
}