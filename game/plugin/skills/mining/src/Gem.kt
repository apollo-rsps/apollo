package org.apollo.game.plugin.skills.mining

enum class Gem(val id: Int) { // TODO add gem drop chances
    UNCUT_SAPPHIRE(1623),
    UNCUT_EMERALD(1605),
    UNCUT_RUBY(1619),
    UNCUT_DIAMOND(1617);

    companion object {
        private val GEMS = Gem.values().associateBy({ it.id }, { it })
        operator fun get(id: Int): Gem? = GEMS[id]
    }
}