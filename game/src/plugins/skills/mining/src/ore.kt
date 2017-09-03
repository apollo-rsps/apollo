package org.apollo.game.plugin.skills.mining

import com.google.common.collect.Maps.asMap

/*
Thanks to Mikey` <http://www.rune-server.org/members/mikey%60/> for helping
to find some of the item/object IDs, minimum levels and experiences.
Thanks to Clifton <http://www.rune-server.org/members/clifton/> for helping
to find some of the expired object IDs.
 */

data class Ore(val id: Int, val objects: Map<Int, Int>, val level: Int, val exp: Double, val respawn: Int, val chance: Double, val chanceOffset: Boolean)


val ORES = mutableMapOf<Int, Ore>()
val EXPIRED_ORES = mutableMapOf<Int, Boolean>()

val CLAY_OBJECTS = mapOf(
        2180 to 450,
        2109 to 451,
        14904 to 14896,
        14905 to 14897
)

val COPPER_OBJECTS = mapOf(
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

val TIN_OBJECTS = mapOf(
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

val IRON_OBJECTS = mapOf(
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

val COAL_OBJECTS = mapOf(
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

val SILVER_OBJECTS = mapOf (
        11948 to 11555,
        11949 to 11556,
        11950 to 11557,
        2100 to 450,
        2101 to 451
)

val GOLD_OBJECTS = mapOf(
        11951 to 11555,
        11952 to 11556,
        11953 to 11557,
        2098 to 450,
        2099 to 451
)

val MITHRIL_OBJECTS = mapOf(
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

val ADAMANT_OBJECTS = mapOf(
        11939 to 11552,
        11940 to 11553,
        11941 to 11554,
        2104 to 450,
        2105 to 451,
        14862 to 14832,
        14863 to 14833,
        14864 to 14834
)

val RUNITE_OBJECTS = mapOf(
        2106 to 450,
        2107 to 451,
        14859 to 14832,
        14860 to 14833,
        14861 to 14834
)


val ORE_OBJECTS = arrayOf(
        Ore(434, CLAY_OBJECTS, 1, 5.0, 3, 0.0085, true), // clay
        Ore(436, COPPER_OBJECTS, 1, 17.5, 6, 0.0085, true), // copper
        Ore(438, TIN_OBJECTS, 1, 17.5, 6, 0.0085, true), // tin
        Ore(440, IRON_OBJECTS, 15, 35.0, 16, 0.0085, true), // iron
        Ore(453, COAL_OBJECTS, 30, 50.0, 100, 0.004, false), // coal
        Ore(444, GOLD_OBJECTS, 40, 65.0, 200, 0.003, false), // gold
        Ore(442, SILVER_OBJECTS, 20, 40.0, 200, 0.0085, false), // silver
        Ore(447, MITHRIL_OBJECTS, 55, 80.0, 400, 0.002, false), // mithril
        Ore(449, ADAMANT_OBJECTS, 70, 95.0, 800, 0.001, false), // adamant
        Ore(451, RUNITE_OBJECTS, 85, 125.0, 2500, 0.0008, false) // runite
)
