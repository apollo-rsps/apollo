package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player

enum class Talisman(val id: Int, val altar: Position) {
    AIR_TALISMAN(1438, Position(2985, 3292)),
    EARTH_TALISMAN(1440, Position(3306, 3474)),
    FIRE_TALISMAN(1442, Position(3313, 3255)),
    WATER_TALISMAN(1444, Position(3185, 3165)),
    BODY_TALISMAN(1446, Position(3053, 3445)),
    MIND_TALISMAN(1448, Position(2982, 3514)),
    CHAOS_TALISMAN(1452, Position(3059, 3590)),
    COSMIC_TALISMAN(1454, Position(2408, 4377)),
    DEATH_TALISMAN(1456, Position(0, 0)),
    LAW_TALISMAN(1458, Position(2858, 3381)),
    NATURE_TALISMAN(1462, Position(2869, 3019));

    companion object {
        private val TALISMANS = Talisman.values()

        fun findById(id: Int): Talisman? = TALISMANS.find { talisman -> talisman.id == id }
    }

    fun sendProximityMessageTo(player: Player) {
        if (altar.isWithinDistance(player.position, 10)) {
            player.sendMessage("Your talisman glows brightly.")
            return
        }

        var direction = if (player.position.y > altar.y) "North" else "South"
        direction += if (player.position.x > altar.x) "-East" else "-West"

        player.sendMessage("The talisman pulls toward the $direction")
    }
}