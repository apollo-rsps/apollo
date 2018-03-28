package org.apollo.game.plugin.skills.woodcutting

import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.woodcutting

enum class Axe(val id: Int, val level: Int, animation: Int, val pulses: Int) {
    BRONZE(id = 1351, level = 1, animation = 879, pulses = 8),
    IRON(id = 1349, level = 1, animation = 877, pulses = 7),
    STEEL(id = 1353, level = 6, animation = 875, pulses = 6),
    BLACK(id = 1361, level = 11, animation = 873, pulses = 6),
    MITHRIL(id = 1355, level = 21, animation = 871, pulses = 5),
    ADAMANT(id = 1357, level = 31, animation = 869, pulses = 4),
    RUNE(id = 1359, level = 41, animation = 867, pulses = 3);

    val animation = Animation(animation)

    companion object {
        private val AXES = Axe.values().sortedByDescending { it.level }

        fun bestFor(player: Player): Axe? {
            return AXES.asSequence()
                .filter { it.level <= player.woodcutting.current }
                .filter { player.equipment.contains(it.id) || player.inventory.contains(it.id) }
                .firstOrNull()
        }
    }
}
