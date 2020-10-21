package org.apollo.game.plugin.skills.mining

/* Chance info provided by https://oldschool.runescape.wiki/w/Gems
 */

enum class Gem(val id: Int,
               val chance: Double,
               val chanceWithGlory: Double
) {
    UNCUT_SAPPHIRE(1623, 0.00390625, 0.01162790698),
    UNCUT_EMERALD(1605, 0.00390625, 0.01162790698),
    UNCUT_RUBY(1619, 0.00390625, 0.01162790698),
    UNCUT_DIAMOND(1617, 0.00390625, 0.01162790698);

    companion object {
        private val GEMS = Gem.values().associateBy({ it.id }, { it })
        operator fun get(id: Int): Gem? = GEMS[id]
    }
}