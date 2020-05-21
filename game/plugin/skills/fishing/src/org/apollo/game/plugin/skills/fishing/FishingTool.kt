package org.apollo.game.plugin.skills.fishing

import org.apollo.game.model.Animation
import org.apollo.game.plugin.api.Definitions

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
    val formattedName by lazy { Definitions.item(id).name.toLowerCase() }
}