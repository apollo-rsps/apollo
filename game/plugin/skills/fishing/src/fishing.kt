package org.apollo.game.plugin.skills.fishing

import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.Animation
import org.apollo.game.plugin.skills.fishing.Fish.*
import org.apollo.game.plugin.skills.fishing.FishingTool.*
import java.util.Random

/**
 * A fish that can be gathered using the fishing skill.
 */
enum class Fish(val id: Int, val level: Int, val experience: Double) {
    SHRIMP(317, 1, 10.0),
    SARDINE(327, 5, 20.0),
    MACKEREL(353, 16, 20.0),
    HERRING(345, 10, 30.0),
    ANCHOVY(321, 15, 40.0),
    TROUT(335, 20, 50.0),
    COD(341, 23, 45.0),
    PIKE(349, 25, 60.0),
    SALMON(331, 30, 70.0),
    TUNA(359, 35, 80.0),
    LOBSTER(377, 40, 90.0),
    BASS(363, 46, 100.0),
    SWORDFISH(371, 50, 100.0),
    SHARK(383, 76, 110.0);

    /**
     * The name of this fish, formatted so it can be inserted into a message.
     */
    val formattedName = ItemDefinition.lookup(id).name.toLowerCase()

}

/**
 * A tool used to gather [Fish] from a [FishingSpot].
 */
enum class FishingTool(val id: Int, animation: Int, val message: String, val bait: Int, val baitName: String?) {
    LOBSTER_CAGE(301, 619, "You attempt to catch a lobster..."),
    SMALL_NET(303, 620, "You cast out your net..."),
    BIG_NET(305, 620, "You cast out your net..."),
    HARPOON(311, 618, "You start harpooning fish..."),
    FISHING_ROD(307, 622, "You attempt to catch a fish...", 313, "feathers"),
    FLY_FISHING_ROD(309, 622, "You attempt to catch a fish...", 314, "fishing bait");

    @Suppress("unused") // IntelliJ bug, doesn't detect that this constructor is used
    constructor(id: Int, animation: Int, message: String) : this(id, animation, message, -1, null)

    /**
     * The [Animation] played when fishing with this tool.
     */
    val animation: Animation = Animation(animation)

    /**
     * The name of this tool, formatted so it can be inserted into a message.
     */
    val formattedName = ItemDefinition.lookup(id).name.toLowerCase()

}

/**
 * A spot that can be fished from.
 */
enum class FishingSpot(val npc: Int, private val first: Option, private val second: Option) {
    ROD(309, Option.of(FLY_FISHING_ROD, TROUT, SALMON), Option.of(FISHING_ROD, PIKE)),
    CAGE_HARPOON(312, Option.of(LOBSTER_CAGE, LOBSTER), Option.of(HARPOON, TUNA, SWORDFISH)),
    NET_HARPOON(313, Option.of(BIG_NET, MACKEREL, COD), Option.of(HARPOON, BASS, SHARK)),
    NET_ROD(316, Option.of(SMALL_NET, SHRIMP, ANCHOVY), Option.of(FISHING_ROD, SARDINE, HERRING));

    companion object {

        private val FISHING_SPOTS = FishingSpot.values().associateBy({ it.npc }, { it })

        /**
         * Returns the [FishingSpot] with the specified [id], or `null` if the spot does not exist.
         */
        fun lookup(id: Int): FishingSpot? = FISHING_SPOTS[id]

    }

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

        companion object {

            fun of(tool: FishingTool, primary: Fish): Option = Single(tool, primary)

            fun of(tool: FishingTool, primary: Fish, secondary: Fish): Option {
                return when {
                    primary.level < secondary.level -> Pair(tool, primary, secondary)
                    else -> Pair(tool, secondary, primary)
                }
            }

        }

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

            companion object {

                val random = Random()

                /**
                 * The weighting factor that causes the lower-level fish to be returned more frequently.
                 */
                const val WEIGHTING = 70

            }

            override val level = Math.min(primary.level, secondary.level)

            override fun sample(level: Int): Fish {
                if (secondary.level > level) {
                    return primary
                }

                return when {
                    random.nextInt(100) < WEIGHTING -> primary
                    else -> secondary
                }
            }

        }

    }

}
