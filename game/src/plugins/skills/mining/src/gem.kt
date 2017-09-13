package org.apollo.game.plugin.skills.mining

enum class Gem(val id: Int, val chance: Int) {
    UNCUT_SAPPHIRE(1623, 0),
    UNCUT_EMERALD(1605, 0),
    UNCUT_RUBY(1619, 0),
    UNCUT_DIAMOND(1617, 0)
}

fun lookupGem(id: Int): Gem? = Gem.values().find { it.id == id }
