package org.apollo.game.plugin.skills.fishing

import org.apollo.game.model.Animation
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.fishing.Fish.*
import org.apollo.game.plugin.skills.fishing.FishingTool.*

/**
 * A fish that can be gathered using the fishing skill.
 */
enum class Fish(val id: Int, val level: Int, val experience: Double, catchSuffix: String? = null) {
    SHRIMPS(id = 317, level = 1, experience = 10.0, catchSuffix = "some shrimp."),
    SARDINE(id = 327, level = 5, experience = 20.0),
    MACKEREL(id = 353, level = 16, experience = 20.0),
    HERRING(id = 345, level = 10, experience = 30.0),
    ANCHOVIES(id = 321, level = 15, experience = 40.0, catchSuffix = "some anchovies."),
    TROUT(id = 335, level = 20, experience = 50.0),
    COD(id = 341, level = 23, experience = 45.0),
    PIKE(id = 349, level = 25, experience = 60.0),
    SALMON(id = 331, level = 30, experience = 70.0),
    TUNA(id = 359, level = 35, experience = 80.0),
    LOBSTER(id = 377, level = 40, experience = 90.0),
    BASS(id = 363, level = 46, experience = 100.0),
    SWORDFISH(id = 371, level = 50, experience = 100.0),
    SHARK(id = 383, level = 76, experience = 110.0, catchSuffix = "a shark!");

    /**
     * The name of this fish, formatted so it can be inserted into a message.
     */
    val catchMessage = "You catch ${catchSuffix ?: "a ${catchName()}."}"

    private fun catchName() = Definitions.item(id)!!.name.toLowerCase().removePrefix("raw ")

}

/**
 * A tool used to gather [Fish] from a [FishingSpot].
 */
enum class FishingTool(
    val message: String,
    val id: Int,
    animation: Int,
    val bait: Int = -1,
    val baitName: String? = null
) {
    LOBSTER_CAGE("You attempt to catch a lobster...", id = 301, animation = 619),
    SMALL_NET("You cast out your net...", id = 303, animation = 620),
    BIG_NET("You cast out your net...", id = 305, animation = 620),
    HARPOON("You start harpooning fish...", id = 311, animation = 618),
    FISHING_ROD("You attempt to catch a fish...", id = 307, animation = 622, bait = 313, baitName = "feathers"),
    FLY_FISHING_ROD("You attempt to catch a fish...", id = 309, animation = 622, bait = 314, baitName = "fishing bait");

    /**
     * The [Animation] played when fishing with this tool.
     */
    val animation: Animation = Animation(animation)

    /**
     * The name of this tool, formatted so it can be inserted into a message.
     */
    val formattedName = Definitions.item(id)!!.name.toLowerCase()

}

/**
 * A spot that can be fished from.
 */
enum class FishingSpot(val npc: Int, private val first: Option, private val second: Option) {
    ROD(309, Option.of(FLY_FISHING_ROD, TROUT, SALMON), Option.of(FISHING_ROD, PIKE)),
    CAGE_HARPOON(312, Option.of(LOBSTER_CAGE, LOBSTER), Option.of(HARPOON, TUNA, SWORDFISH)),
    NET_HARPOON(313, Option.of(BIG_NET, MACKEREL, COD), Option.of(HARPOON, BASS, SHARK)),
    NET_ROD(316, Option.of(SMALL_NET, SHRIMPS, ANCHOVIES), Option.of(FISHING_ROD, SARDINE, HERRING));

    /**
     * Returns the [FishingSpot.Option] associated with the specified action id.
     */
    fun option(action: Int): Option {
        return when (action) {
            1 -> first
            3 -> second
            else -> throw UnsupportedOperationException("Unexpected fishing spot option $action.")
        }
    }

    /**
     * An option at a [FishingSpot] (e.g. either "rod fishing" or "net fishing").
     */
    sealed class Option {

        /**
         * The tool used to obtain fish
         */
        abstract val tool: FishingTool

        /**
         * The minimum level required to obtain fish.
         */
        abstract val level: Int

        /**
         * Samples a [Fish], randomly (with weighting) returning one (that can be fished by the player).
         *
         * @param level The fishing level of the player.
         */
        abstract fun sample(level: Int): Fish

        /**
         * A [FishingSpot] [Option] that can only provide a single type of fish.
         */
        private data class Single(override val tool: FishingTool, val primary: Fish) : Option() {
            override val level = primary.level

            override fun sample(level: Int): Fish = primary
        }

        /**
         * A [FishingSpot] [Option] that can provide a two different types of fish.
         */
        private data class Pair(override val tool: FishingTool, val primary: Fish, val secondary: Fish) : Option() {
            override val level = Math.min(primary.level, secondary.level)

            override fun sample(level: Int): Fish {
                return if (level < secondary.level || rand(100) < WEIGHTING) {
                    primary
                } else {
                    secondary
                }
            }

            private companion object {
                /**
                 * The weighting factor that causes the lower-level fish to be returned more frequently.
                 */
                private const val WEIGHTING = 70
            }
        }

        companion object {

            fun of(tool: FishingTool, primary: Fish): Option = Single(tool, primary)

            fun of(tool: FishingTool, primary: Fish, secondary: Fish): Option {
                return when {
                    primary.level < secondary.level -> Pair(tool, primary, secondary)
                    else -> Pair(tool, secondary, primary)
                }
            }

        }

    }

    companion object {
        private val FISHING_SPOTS = FishingSpot.values().associateBy(FishingSpot::npc)

        /**
         * Returns the [FishingSpot] with the specified [id], or `null` if the spot does not exist.
         */
        fun lookup(id: Int): FishingSpot? = FISHING_SPOTS[id]
    }

}
