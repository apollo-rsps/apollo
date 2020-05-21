package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.plugin.skill.runecrafting.Altar.*

interface Rune {
    /**
     * The item id of the rune.
     */
    val id: Int

    /**
     * The [Altar] this rune must be crafted at.
     */
    val altar: Altar

    /**
     * The runecrafting level required to craft runes of this type.
     */
    val level: Int

    /**
     * The amount of experience rewarded from crafting a single rune of this type.
     */
    val xp: Double

    /**
     * Get the multiplier that is applied to the number of runes the player crafts for their runecrafting level.
     *
     * [playerLevel] - The players current runecrafting level.
     */
    fun getBonusMultiplier(playerLevel: Int): Double
}

enum class DefaultRune(
    override val id: Int,
    override val altar: Altar,
    override val level: Int,
    override val xp: Double
) : Rune {
    AIR_RUNE(556, AIR_ALTAR, 1, 5.0),
    MIND_RUNE(558, MIND_ALTAR, 1, 5.5),
    WATER_RUNE(555, WATER_ALTAR, 5, 6.0),
    EARTH_RUNE(557, EARTH_ALTAR, 9, 6.5),
    FIRE_RUNE(554, FIRE_ALTAR, 14, 7.0),
    BODY_RUNE(559, BODY_ALTAR, 20, 7.5),
    COSMIC_RUNE(564, COSMIC_ALTAR, 27, 8.0),
    CHAOS_RUNE(562, CHAOS_ALTAR, 35, 8.5),
    NATURE_RUNE(561, NATURE_ALTAR, 44, 9.0),
    LAW_RUNE(563, LAW_ALTAR, 54, 9.5),
    DEATH_RUNE(560, DEATH_ALTAR, 65, 10.0);

    override fun getBonusMultiplier(playerLevel: Int): Double = when (this) {
        DefaultRune.AIR_RUNE -> (Math.floor((playerLevel / 11.0)) + 1)
        DefaultRune.MIND_RUNE -> (Math.floor((playerLevel / 14.0)) + 1)
        DefaultRune.WATER_RUNE -> (Math.floor((playerLevel / 19.0)) + 1)
        DefaultRune.EARTH_RUNE -> (Math.floor((playerLevel / 26.0)) + 1)
        DefaultRune.FIRE_RUNE -> (Math.floor((playerLevel / 35.0)) + 1)
        DefaultRune.BODY_RUNE -> (Math.floor((playerLevel / 46.0)) + 1)
        DefaultRune.COSMIC_RUNE -> (Math.floor((playerLevel / 59.0)) + 1)
        DefaultRune.CHAOS_RUNE -> (Math.floor((playerLevel / 74.0)) + 1)
        DefaultRune.NATURE_RUNE -> (Math.floor((playerLevel / 91.0)) + 1)
        DefaultRune.LAW_RUNE -> 1.0
        DefaultRune.DEATH_RUNE -> 1.0
    }
}
