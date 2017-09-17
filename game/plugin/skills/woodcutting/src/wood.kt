package org.apollo.game.plugin.skills.woodcutting

private val NORMAL_STUMP = 1342
private val ACHEY_STUMP = 3371
private val OAK_STUMP = 1342
private val WILLOW_STUMP = 1342
private val TEAK_STUMP = 1342
private val MAPLE_STUMP = 1342
private val MAHOGANY_STUMP = 1342
private val YEW_STUMP = 1342
private val MAGIC_STUMP = 1324

private val NORMAL_OBJECTS = hashSetOf(
        1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1285, 1286, 1289, 1290, 1291, 1315,
        1316, 1318, 1330, 1331, 1332, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882,
        3883, 5902, 5903, 5904, 10041
)

private val ACHEY_OBJECTS = hashSetOf(
        2023
)

private val OAK_OBJECTS = hashSetOf(
        1281, 3037
)

private val WILLOW_OBJECTS = hashSetOf(
        5551, 5552, 5553
)

private val TEAK_OBJECTS = hashSetOf(
        9036
)


private val MAPLE_OBJECTS = hashSetOf(
        1307, 4674
)

private val MAHOGANY_OBJECTS = hashSetOf(
        9034
)

private val YEW_OBJECTS = hashSetOf(
        1309
)

private val MAGIC_OBJECTS = hashSetOf(
        1292, 1306
)

/**
 * values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 */
enum class Tree(val id: Int, val objects: HashSet<Int>, val stump: Int, val level: Int, val exp: Double, val chance: Double) {
    NORMAL(1511, NORMAL_OBJECTS, NORMAL_STUMP, 1, 25.0, 100.0),
    ACHEY(2862, ACHEY_OBJECTS, ACHEY_STUMP, 1, 25.0, 100.0),
    OAK(1521, OAK_OBJECTS, OAK_STUMP, 15, 37.5, 0.125),
    WILLOW(1519, WILLOW_OBJECTS, WILLOW_STUMP, 30, 67.5, 0.125),
    TEAK(6333, TEAK_OBJECTS, TEAK_STUMP, 35, 85.0, 0.125),
    MAPLE(1517, MAPLE_OBJECTS, MAPLE_STUMP, 45, 100.0, 0.125),
    MAHOGANY(6332, MAHOGANY_OBJECTS, MAHOGANY_STUMP, 50, 125.0, 0.125),
    YEW(1515, YEW_OBJECTS, YEW_STUMP, 60, 175.0, 0.125),
    MAGIC(1513, MAGIC_OBJECTS, MAGIC_STUMP, 75, 250.0, 0.125);

    companion object {
        private val TREES = Tree.values().flatMap { tree -> tree.objects.map { Pair(it, tree) } }.toMap()
        fun lookup(id: Int): Tree? = TREES[id]
    }
}


