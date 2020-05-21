package org.apollo.game.plugin.skills.mining

/*
Thanks to Mikey` <http://www.rune-server.org/members/mikey%60/> for helping
to find some of the item/object IDs, minimum levels and experiences.
Thanks to Clifton <http://www.rune-server.org/members/clifton/> for helping
to find some of the expired object IDs.
 */
/**
 * Chance values thanks to: http://runescape.wikia.com/wiki/Talk:Mining#Mining_success_rate_formula
 * Respawn times and xp thanks to: http://oldschoolrunescape.wikia.com/wiki/
 */
enum class Ore(
    val objects: Map<Int, Int>,
    val id: Int,
    val level: Int,
    val exp: Double,
    val respawn: Int,
    val chance: Double,
    val chanceOffset: Double = 0.0
) {
    CLAY(CLAY_OBJECTS, id = 434, level = 1, exp = 5.0, respawn = 1, chance = 0.0085, chanceOffset = 0.45),
    COPPER(COPPER_OBJECTS, id = 436, level = 1, exp = 17.5, respawn = 4, chance = 0.0085, chanceOffset = 0.45),
    TIN(TIN_OBJECTS, id = 438, level = 1, exp = 17.5, respawn = 4, chance = 0.0085, chanceOffset = 0.45),
    IRON(IRON_OBJECTS, id = 440, level = 15, exp = 35.0, respawn = 9, chance = 0.0085, chanceOffset = 0.45),
    COAL(COAL_OBJECTS, id = 453, level = 30, exp = 50.0, respawn = 50, chance = 0.004),
    GOLD(GOLD_OBJECTS, id = 444, level = 40, exp = 65.0, respawn = 100, chance = 0.003),
    SILVER(SILVER_OBJECTS, id = 442, level = 20, exp = 40.0, respawn = 100, chance = 0.0085),
    MITHRIL(MITHRIL_OBJECTS, id = 447, level = 55, exp = 80.0, respawn = 200, chance = 0.002),
    ADAMANT(ADAMANT_OBJECTS, id = 449, level = 70, exp = 95.0, respawn = 800, chance = 0.001),
    RUNITE(RUNITE_OBJECTS, id = 451, level = 85, exp = 125.0, respawn = 1_200, chance = 0.0008);

    companion object {
        private val ORE_ROCKS = Ore.values().flatMap { ore -> ore.objects.map { Pair(it.key, ore) } }.toMap()
        private val EXPIRED_ORE = Ore.values().flatMap { ore -> ore.objects.map { Pair(it.value, ore) } }.toMap()

        fun fromRock(id: Int): Ore? = ORE_ROCKS[id]
        fun fromExpiredRock(id: Int): Ore? = EXPIRED_ORE[id]
    }
}

// Maps from regular rock id to expired rock id.

val CLAY_OBJECTS = mapOf(
    2108 to 450,
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
    2090 to 450,
    2091 to 451,
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

val SILVER_OBJECTS = mapOf(
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