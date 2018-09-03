package org.apollo.game.plugin.skill.runecrafting

import org.apollo.game.model.Position

enum class Altar(val entranceId: Int, val craftingId: Int, val portalId: Int, val entrance: Position, val exit: Position, val center: Position) {
    AIR_ALTAR(2452, 2478, 2465, Position(2841, 4829), Position(2983, 3292), Position(2844, 4834)),
    MIND_ALTAR(2453, 2479, 2466, Position(2793, 4828), Position(2980, 3514), Position(2786, 4841)),
    WATER_ALTAR(2454, 2480, 2467, Position(2726, 4832), Position(3187, 3166), Position(2716, 4836)),
    EARTH_ALTAR(2455, 2481, 2468, Position(2655, 4830), Position(3304, 3474), Position(2658, 4841)),
    FIRE_ALTAR(2456, 2482, 2469, Position(2574, 4849), Position(3311, 3256), Position(2585, 4838)),
    BODY_ALTAR(2457, 2483, 2470, Position(2524, 4825), Position(3051, 3445), Position(2525, 4832)),
    COSMIC_ALTAR(2458, 2484, 2471, Position(2142, 4813), Position(2408, 4379), Position(2142, 4833)),
    LAW_ALTAR(2459, 2485, 2472, Position(2464, 4818), Position(2858, 3379), Position(2464, 4832)),
    NATURE_ALTAR(2460, 2486, 2473, Position(2400, 4835), Position(2867, 3019), Position(2400, 4841)),
    CHAOS_ALTAR(2461, 2487, 2474, Position(2268, 4842), Position(3058, 3591), Position(2271, 4842)),
    DEATH_ALTAR(2462, 2488, 2475, Position(2208, 4830), Position(3222, 3222), Position(2205, 4836));

    companion object {
        private val ALTARS = Altar.values()

        fun findByEntranceId(id: Int): Altar? = ALTARS.find { Altar -> Altar.entranceId == id }
        fun findByPortalId(id: Int): Altar? = ALTARS.find { Altar -> Altar.portalId == id }
        fun findByCraftingId(id: Int): Altar? = ALTARS.find { Altar -> Altar.craftingId == id }
    }
}