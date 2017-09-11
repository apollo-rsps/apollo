package org.apollo.game.plugin.skills.mining


/**
 * values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 */
enum class Wood(val id: Int, val objects: Map<Int, Int>, val level: Int, val exp: Double, val respawn: Int, val chance: Double) {
    NORMAL(434, NORMAL_OBJECTS, 1, 25.0, 1, 100.0),
    ACHEY(436, ACHEY_OBJECTS, 1, 25.0, 4, 100.0),
    OAK(438, OAK_OBJECTS, 15, 37.5, 4, 0.125),
    WILLOW(440, WILLOW_OBJECTS, 30, 67.5, 9, 0.125),
    TEAK(453, TEAK_OBJECTS, 35, 85.0, 50, 0.125),
    MAPLE(444, MAPLE_OBJECTS, 45, 100.0, 100, 0.125),
    HOLLOW(442, HOLLOW_OBJECTS, 45, 82.5, 100, 0.125),
    MAHOGANY(447, MAHOGANY_OBJECTS, 50, 125.0, 200,0.125),
    ARTIC_PINE(449, ARTIC_PIN_OBJECTS, 54, 40.0, 800, 0.125),
    YEW(451, YEW_OBJECTS, 60, 175.0, 1200, 0.125),
    MAGIC(451, MAGIC_OBJECTS, 75, 250.0, 1200, 0.125),
    REDWOOD(451, REDWOOD_OBJECTS, 90, 380.0, 1200, 0.125)
}

fun lookupWood(id: Int): Wood? {
    for (wood in Wood.values()) {
        if (wood.id == id) {
            return wood
        }
    }
    return null
}

fun lookupTree(id: Int): Wood? {
    for (wood in Wood.values()) {
        for (tree in wood.objects) {
            if (tree.key == id) {
                return wood
            }
        }
    }
    return null
}

val NORMAL_OBJECTS = mapOf(
        0 to 0
)

val ACHEY_OBJECTS = mapOf(
        0 to 0
)

val OAK_OBJECTS = mapOf(
        0 to 0
)

val WILLOW_OBJECTS = mapOf(
        0 to 0
)

val TEAK_OBJECTS = mapOf(
        0 to 0
)

val HOLLOW_OBJECTS = mapOf (
        0 to 0
)

val MAPLE_OBJECTS = mapOf(
        0 to 0
)

val MAHOGANY_OBJECTS = mapOf(
        0 to 0
)

val ARTIC_PIN_OBJECTS = mapOf(
        0 to 0
)

val YEW_OBJECTS = mapOf(
        0 to 0
)

val MAGIC_OBJECTS = mapOf(
        0 to 0
)

val REDWOOD_OBJECTS = mapOf(
0 to 0
)

