package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.action.DistancedAction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player

class TeleportToAltarAction(val player: Player, val start: Position, val distance: Int, val end: Position) : DistancedAction<Player>(0, true, player, start, distance) {
    override fun executeAction() {
        player.teleport(end)
        stop()
    }
}