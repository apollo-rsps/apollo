package org.apollo.game.plugin.skills.mining

import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.mining

enum class Pickaxe(val id: Int, val level: Int, animation: Int, val pulses: Int) {
    BRONZE(id = 1265, level = 1, animation = 625, pulses = 8),
    ITRON(id = 1267, level = 1, animation = 626, pulses = 7),
    STEEL(id = 1269, level = 6, animation = 627, pulses = 6),
    MITHRIL(id = 1273, level = 21, animation = 629, pulses = 5),
    ADAMANT(id = 1271, level = 31, animation = 628, pulses = 4),
    RUNE(id = 1275, level = 41, animation = 624, pulses = 3);

    val animation = Animation(animation)

    companion object {
        private val PICKAXES = Pickaxe.values().sortedByDescending { it.level }

        fun bestFor(player: Player): Pickaxe? {
            return PICKAXES.asSequence()
                .filter { it.level <= player.mining.current }
                .filter { player.equipment.contains(it.id) || player.inventory.contains(it.id) }
                .firstOrNull()
        }
    }
}
