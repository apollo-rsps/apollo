package org.apollo.game.plugin.skills.fishing

import org.apollo.game.plugin.api.Definitions

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
    val catchMessage by lazy { "You catch ${catchSuffix ?: "a $catchName."}" }

    private val catchName by lazy { Definitions.item(id).name.toLowerCase().removePrefix("raw ") }
}
