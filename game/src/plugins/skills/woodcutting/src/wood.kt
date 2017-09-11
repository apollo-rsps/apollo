package org.apollo.game.plugin.skills.mining


/**
 * values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 */
enum class Wood(val id: Int, val objects: Map<Int, Int>, val level: Int, val exp: Double, val chance: Double) {
    NORMAL(1511, NORMAL_OBJECTS, 1, 25.0, 100.0),
    ACHEY(2862, ACHEY_OBJECTS, 1, 25.0, 100.0),
    OAK(1521, OAK_OBJECTS, 15, 37.5, 0.125),
    WILLOW(1519, WILLOW_OBJECTS, 30, 67.5, 0.125),
    TEAK(6333, TEAK_OBJECTS, 35, 85.0, 0.125),
    MAPLE(1517, MAPLE_OBJECTS, 45, 100.0, 0.125),
    MAHOGANY(6332, MAHOGANY_OBJECTS, 50, 125.0,0.125),
    YEW(1515, YEW_OBJECTS, 60, 175.0, 0.125),
    MAGIC(1513, MAGIC_OBJECTS, 75, 250.0, 0.125),
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


val MAPLE_OBJECTS = mapOf(
        0 to 0
)

val MAHOGANY_OBJECTS = mapOf(
        0 to 0
)


val YEW_OBJECTS = mapOf(
        0 to 0
)

val MAGIC_OBJECTS = mapOf(
        0 to 0
)



