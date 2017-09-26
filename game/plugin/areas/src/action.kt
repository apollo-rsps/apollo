package org.apollo.game.plugins.area

/**
 * Defines an area action using the DSL.
 */
fun action(@Suppress("UNUSED_PARAMETER") name: String, builder: ActionBuilder.() -> Unit) {
    val listener = ActionBuilder()
    builder(listener)

    actions.add(listener.build())
}

/**
 * The [Set] of ([Area], [AreaAction]) [Pair]s.
 */
val actions = mutableSetOf<Pair<Area, AreaAction>>()

class AreaAction(val entrance: AreaListener, val inside: AreaListener, val exit: AreaListener)