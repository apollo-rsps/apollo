package org.apollo.game.plugin.skills.woodcutting


/**
 * values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 */
enum class Wood(val id: Int, val objects: IntArray, val stump: Int, val level: Int, val exp: Double, val chance: Double) {
    NORMAL(1511, NORMAL_OBJECTS, NORMAL_STUMP, 1, 25.0, 100.0),
    ACHEY(2862, ACHEY_OBJECTS, ACHEY_STUMP, 1, 25.0, 100.0),
    OAK(1521, OAK_OBJECTS, OAK_STUMP, 15, 37.5, 0.125),
    WILLOW(1519, WILLOW_OBJECTS, WILLOW_STUMP, 30, 67.5, 0.125),
    TEAK(6333, TEAK_OBJECTS, TEAK_STUMP, 35, 85.0, 0.125),
    MAPLE(1517, MAPLE_OBJECTS, MAPLE_STUMP, 45, 100.0, 0.125),
    MAHOGANY(6332, MAHOGANY_OBJECTS, MAHOGANY_STUMP, 50, 125.0,0.125),
    YEW(1515, YEW_OBJECTS, YEW_STUMP, 60, 175.0, 0.125),
    MAGIC(1513, MAGIC_OBJECTS, MAGIC_STUMP, 75, 250.0, 0.125),
}

val WOOD = Wood.values()

fun lookupWood(id: Int): Wood? =WOOD.find { it.id == id }

fun lookupTree(id: Int): Wood? {
    for (wood in WOOD) {
        for (tree in wood.objects) {
            if (tree == id) {
                return wood
            }
        }
    }
    return null
}

val NORMAL_STUMP = 1342
val ACHEY_STUMP = 3371
val OAK_STUMP = 1342
val WILLOW_STUMP = 1342
val TEAK_STUMP = 1342
val MAPLE_STUMP = 1342
val MAHOGANY_STUMP = 1342
val YEW_STUMP = 1342
val MAGIC_STUMP = 1324

val NORMAL_OBJECTS = intArrayOf(
        1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1285, 1286, 1289, 1290, 1291, 1315,
        1316, 1318, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882,
        3883, 5902, 5903, 5904, 10041
)

val ACHEY_OBJECTS = intArrayOf(
        2023
)

val OAK_OBJECTS = intArrayOf(
        1281, 3037
)

val WILLOW_OBJECTS = intArrayOf(
        5551, 5552, 5553
)

val TEAK_OBJECTS = intArrayOf(
        9036
)


val MAPLE_OBJECTS = intArrayOf(
        1307, 4674
)

val MAHOGANY_OBJECTS = intArrayOf(
        9034
)


val YEW_OBJECTS = intArrayOf(
        1309
)

val MAGIC_OBJECTS = intArrayOf(
        1292, 1306
)



