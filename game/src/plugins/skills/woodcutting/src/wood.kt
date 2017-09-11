package org.apollo.game.plugin.skills.mining

/*
Thanks to Mikey` <http://www.rune-server.org/members/mikey%60/> for helping
to find some of the item/object IDs, minimum levels and experiences.
Thanks to Clifton <http://www.rune-server.org/members/clifton/> for helping
to find some of the expired object IDs.
 */


/**
 * values thanks to: http://oldschoolrunescape.wikia.com/wiki/Woodcutting
 */
enum class Wood(val id: Int, val objects: Map<Int, Int>, val level: Int, val exp: Double, val respawn: Int, val chance: Double) {
    NORMAL(434, NOMRAL_OBJECTS, 1, 25.0, 1, 100.0),
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

val NOMRAL_OBJECTS = mapOf(
        2108 to 450,
        2109 to 451,
        14904 to 14896,
        14905 to 14897
)

val ACHEY_OBJECTS = mapOf(
        11960 to 11555,
        11961 to 11556,
        11962 to 11557,
        11936 to 11552,
        11937 to 11553,
        11938 to 11554,
        2090  to 450,
        2091  to 451,
        14906 to 14898,
        14907 to 14899,
        14856 to 14832,
        14857 to 14833,
        14858 to 14834
)

val OAK_OBJECTS = mapOf(
        11597 to 11555,
        11958 to 11556,
        11959 to 11557,
        11933 to 11552,
        11934 to 11553,
        11935 to 11554,
        2094 to 450,
        2095 to 451,
        14092 to 14894,
        14903 to 14895
)

val WILLOW_OBJECTS = mapOf(
        11954 to 11555,
        11955 to 11556,
        11956 to 11557,
        2092 to 450,
        2093 to 451,
        14900 to 14892,
        14901 to 14893,
        14913 to 14915,
        14914 to 14916
)

val TEAK_OBJECTS = mapOf(
        11963 to 11555,
        11964 to 11556,
        11965 to 11557,
        11930 to 11552,
        11931 to 11553,
        11932 to 11554,
        2096 to 450,
        2097 to 451,
        14850 to 14832,
        14851 to 14833,
        14852 to 14834
)

val HOLLOW_OBJECTS = mapOf (
        11948 to 11555,
        11949 to 11556,
        11950 to 11557,
        2100 to 450,
        2101 to 451
)

val MAPLE_OBJECTS = mapOf(
        11951 to 11555,
        11952 to 11556,
        11953 to 11557,
        2098 to 450,
        2099 to 451
)

val MAHOGANY_OBJECTS = mapOf(
        11945 to 11555,
        11946 to 11556,
        11947 to 11557,
        11942 to 11552,
        11943 to 11553,
        11944 to 11554,
        2102 to 450,
        2103 to 451,
        14853 to 14832,
        14854 to 14833,
        14855 to 14834
)

val ARTIC_PIN_OBJECTS = mapOf(
        11939 to 11552,
        11940 to 11553,
        11941 to 11554,
        2104 to 450,
        2105 to 451,
        14862 to 14832,
        14863 to 14833,
        14864 to 14834
)

val YEW_OBJECTS = mapOf(
        2106 to 450,
        2107 to 451,
        14859 to 14832,
        14860 to 14833,
        14861 to 14834
)

val MAGIC_OBJECTS = mapOf(
        2106 to 450,
        2107 to 451,
        14859 to 14832,
        14860 to 14833,
        14861 to 14834
)

val REDWOOD_OBJECTS = mapOf(
        2106 to 450,
        2107 to 451,
        14859 to 14832,
        14860 to 14833,
        14861 to 14834
)

