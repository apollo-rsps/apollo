import Altar.*

enum class Rune(val id: Int, val altar: Altar, val level: Int, val xp: Double) {
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

    companion object {
        private val RUNES = Rune.values()

        fun findById(id: Int): Rune? = RUNES.find { rune -> rune.id == id }
        fun findByAltarId(id: Int): Rune? = RUNES.find { rune -> rune.altar.craftingId == id }
    }

    fun getBonus(): Double = when (this) {
        Rune.AIR_RUNE -> (Math.floor((level / 11.0)) + 1)
        Rune.MIND_RUNE -> (Math.floor((level / 14.0)) + 1)
        Rune.WATER_RUNE -> (Math.floor((level / 19.0)) + 1)
        Rune.EARTH_RUNE -> (Math.floor((level / 26.0)) + 1)
        Rune.FIRE_RUNE -> (Math.floor((level / 35.0)) + 1)
        Rune.BODY_RUNE -> (Math.floor((level / 46.0)) + 1)
        Rune.COSMIC_RUNE -> (Math.floor((level / 59.0)) + 1)
        Rune.CHAOS_RUNE -> (Math.floor((level / 74.0)) + 1)
        Rune.NATURE_RUNE -> (Math.floor((level / 91.0)) + 1)
        Rune.LAW_RUNE -> 1.0
        Rune.DEATH_RUNE -> 1.0
    }
}
