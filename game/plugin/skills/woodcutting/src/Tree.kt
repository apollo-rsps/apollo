package org.apollo.game.plugin.skills.woodcutting

/*
 * Values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 * https://twitter.com/JagexKieren/status/713409506273787904
 */

enum class Tree(
    val objects: Set<Int>,
    val id: Int,
    val stump: Int,
    val level: Int,
    val exp: Double,
    val chance: Double
) {
    NORMAL(NORMAL_OBJECTS, id = 1511, stump = 1342, level = 1, exp = 25.0, chance = 100.0),
    ACHEY(ACHEY_OBJECTS, id = 2862, stump = 3371, level = 1, exp = 25.0, chance = 100.0),
    OAK(OAK_OBJECTS, id = 1521, stump = 1342, level = 15, exp = 37.5, chance = 12.5),
    WILLOW(WILLOW_OBJECTS, id = 1519, stump = 1342, level = 30, exp = 67.5, chance = 12.5),
    TEAK(TEAK_OBJECTS, id = 6333, stump = 1342, level = 35, exp = 85.0, chance = 12.5),
    MAPLE(MAPLE_OBJECTS, id = 1517, stump = 1342, level = 45, exp = 100.0, chance = 12.5),
    MAHOGANY(MAHOGANY_OBJECTS, id = 6332, stump = 1342, level = 50, exp = 125.0, chance = 12.5),
    YEW(YEW_OBJECTS, id = 1515, stump = 1342, level = 60, exp = 175.0, chance = 12.5),
    MAGIC(MAGIC_OBJECTS, id = 1513, stump = 1324, level = 75, exp = 250.0, chance = 12.5);

    companion object {
        private val TREES = Tree.values().flatMap { tree -> tree.objects.map { Pair(it, tree) } }.toMap()
        fun lookup(id: Int): Tree? = TREES[id]
    }
}

private val NORMAL_OBJECTS = hashSetOf(
    1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1285, 1286, 1289, 1290, 1291, 1315,
    1316, 1318, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882,
    3883, 5902, 5903, 5904, 10041
)

private val ACHEY_OBJECTS = hashSetOf(2023)
private val OAK_OBJECTS = hashSetOf(1281, 3037)
private val WILLOW_OBJECTS = hashSetOf(5551, 5552, 5553)
private val TEAK_OBJECTS = hashSetOf(9036)
private val MAPLE_OBJECTS = hashSetOf(1307, 4674)
private val MAHOGANY_OBJECTS = hashSetOf(9034)
private val YEW_OBJECTS = hashSetOf(1309)
private val MAGIC_OBJECTS = hashSetOf(1292, 1306)