package org.apollo.game.plugin.skills.mining

data class Gem(val id: Int, val chance: Int)

private val GEMS = mutableMapOf<Int, Gem>()

fun lookupGem(id: Int): Gem? {
    return GEMS[id]
}

fun addGem(id: Int, chance: Int) {
    GEMS.put(id, Gem(id, chance))
}
