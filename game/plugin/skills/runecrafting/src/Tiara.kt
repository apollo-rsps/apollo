package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.plugin.skill.runecrafting.Altar.*
import org.apollo.game.plugin.skill.runecrafting.Talisman.*

enum class Tiara(val id: Int, val altar: Altar, val talisman: Talisman, val configId: Int, val xp: Double) {
    AIR_TIARA(5527, AIR_ALTAR, AIR_TALISMAN, 0, 25.0),
    MIND_TIARA(5529, MIND_ALTAR, MIND_TALISMAN, 1, 27.5),
    WATER_TIARA(5531, WATER_ALTAR, WATER_TALISMAN, 2, 30.0),
    BODY_TIARA(5533, BODY_ALTAR, BODY_TALISMAN, 5, 37.5),
    EARTH_TIARA(5535, EARTH_ALTAR, EARTH_TALISMAN, 3, 32.5),
    FIRE_TIARA(5537, FIRE_ALTAR, FIRE_TALISMAN, 4, 35.0),
    COSMIC_TIARA(5539, COSMIC_ALTAR, COSMIC_TALISMAN, 6, 40.0),
    NATURE_TIARA(5541, NATURE_ALTAR, NATURE_TALISMAN, 8, 45.0),
    CHAOS_TIARA(5543, CHAOS_ALTAR, CHAOS_TALISMAN, 9, 42.5),
    LAW_TIARA(5545, LAW_ALTAR, LAW_TALISMAN, 7, 47.5),
    DEATH_TIARA(5548, DEATH_ALTAR, DEATH_TALISMAN, 10, 50.0);

    companion object {
        private val TIARAS = Tiara.values()

        fun findById(id: Int): Tiara? = TIARAS.find { tiara -> tiara.id == id }
        fun findByAltarId(id: Int): Tiara? = TIARAS.find { tiara -> tiara.altar.entranceId == id }
        fun findByTalismanId(id: Int): Tiara? = TIARAS.find { tiara -> tiara.talisman.id == id }
    }
}