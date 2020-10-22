package org.apollo.game.plugin.skills.mining

/* Chance info provided by https://oldschool.runescape.wiki/w/Gems
 */

enum class Gem(val id: Int,
               val chance: Double,
               val chanceWithGlory: Double
) {
    UNCUT_SAPPHIRE(id = 1623, chance = 0.00390625, chanceWithGlory = 0.01162790698),
    UNCUT_EMERALD(id = 1605, chance = 0.00390625, chanceWithGlory = 0.01162790698),
    UNCUT_RUBY(id = 1619, chance = 0.00390625, chanceWithGlory = 0.01162790698),
    UNCUT_DIAMOND(id = 1617, chance = 0.00390625, chanceWithGlory = 0.01162790698);

    companion object {
        private val GEMS = Gem.values().associateBy({ it.id }, { it })
        operator fun get(id: Int): Gem? = GEMS[id]
    }
}