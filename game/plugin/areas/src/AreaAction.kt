package org.apollo.game.plugins.area

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player

/**
 * A set of actions to execute when a player enters, moves inside, or exits a specific area of the world.
 */
internal class AreaAction(val entrance: AreaListener, val inside: AreaListener, val exit: AreaListener)

/**
 * A function that is invoked when a player enters, moves inside of, or exits an [Area].
 */
typealias AreaListener = Player.(Position) -> Unit

/**
 * Registers an [AreaAction] for the specified [Area] using the builder.
 */
fun action(name: String, area: Area, builder: AreaActionBuilder.() -> Unit) {
    actions += AreaActionBuilder(name, area).apply(builder).build()
}

/**
 * Registers an [AreaAction] for the specified [Area] using the builder.
 *
 * @param predicate The predicate that determines whether or not the given [Position] is inside the [Area].
 */
fun action(name: String, predicate: (Position) -> Boolean, builder: AreaActionBuilder.() -> Unit) {
    val area = object : Area {
        override fun contains(position: Position): Boolean = predicate(position)
    }

    action(name, area, builder)
}

/**
 * Registers an [AreaAction] for the specified [Area] using the builder.
 *
 * @param x The `x` coordinate range, both ends inclusive.
 * @param y The `y` coordinate range, both ends inclusive.
 */
fun action(name: String, x: IntRange, y: IntRange, height: Int = 0, builder: AreaActionBuilder.() -> Unit) {
    val area = RectangularArea(x, y, height)

    action(name, area, builder)
}

/**
 * The [Set] of ([Area], [AreaAction]) [Pair]s.
 */
internal val actions = mutableSetOf<Pair<Area, AreaAction>>()
