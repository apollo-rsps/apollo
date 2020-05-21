package org.apollo.game.plugin.skills.fishing

import org.apollo.game.plugin.api.rand
import org.apollo.game.plugin.skills.fishing.Fish.*

/**
 * A spot that can be fished from.
 */
enum class FishingSpot(val npc: Int, private val first: Option, private val second: Option) {

    ROD(
        npc = 309,
        first = Option.of(tool = FishingTool.FLY_FISHING_ROD, primary = TROUT, secondary = SALMON),
        second = Option.of(tool = FishingTool.FISHING_ROD, primary = PIKE)
    ),

    CAGE_HARPOON(
        npc = 312,
        first = Option.of(tool = FishingTool.LOBSTER_CAGE, primary = LOBSTER),
        second = Option.of(tool = FishingTool.HARPOON, primary = TUNA, secondary = SWORDFISH)
    ),

    NET_HARPOON(
        npc = 313,
        first = Option.of(tool = FishingTool.BIG_NET, primary = MACKEREL, secondary = COD),
        second = Option.of(tool = FishingTool.HARPOON, primary = BASS, secondary = SHARK)
    ),

    NET_ROD(
        npc = 316,
        first = Option.of(tool = FishingTool.SMALL_NET, primary = SHRIMPS, secondary = ANCHOVIES),
        second = Option.of(tool = FishingTool.FISHING_ROD, primary = SARDINE, secondary = HERRING)
    );

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