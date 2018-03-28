package org.apollo.game.plugins.area

import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugins.api.Position.component1
import org.apollo.game.plugins.api.Position.component2
import org.apollo.game.plugins.api.Position.component3

/**
 * An area in the game world.
 */
interface Area {

    /**
     * Returns whether or not the specified [Position] is inside this [Area].
     */
    operator fun contains(position: Position): Boolean

}

private class RectangularArea(val x: IntRange, val y: IntRange, val height: Int) : Area {

    override operator fun contains(position: Position): Boolean {
        val (x, y, z) = position
        return x in this.x && y in this.y && z == height
    }

}

/**
 * A typealias for a function that is invoked when a player enters, moves inside of, or exits an [Area].
 */
internal typealias AreaListener = Player.(Position) -> Unit

/**
 * A builder for ([Area], [AreaAction]) [Pair]s.
 */
class ActionBuilder {

    private var area: Area? = null

    private var entrance: AreaListener = { }

    private var inside: AreaListener = { }

    private var exit: AreaListener = { }

    /**
     * Places the contents of this builder into an ([Area], [AreaAction]) [Pair].
     */
    fun build(): Pair<Area, AreaAction> {
        val area = area ?: throw UnsupportedOperationException("Area must be specified.")
        return Pair(area, AreaAction(entrance, inside, exit))
    }

    /**
     * Sets the [Area] to listen for movement in.
     */
    fun area(contains: (Position) -> Boolean) {
        this.area = object : Area {
            override fun contains(position: Position): Boolean = contains.invoke(position)
        }
    }

    /**
     * Sets the [Area] to listen for movement in. Note that [IntRange]s are (inclusive, _exclusive_), i.e. the upper
     * bound is exclusive.
     */
    fun area(x: IntRange, y: IntRange, height: Int = 0) {
        this.area = RectangularArea(x, y, height)
    }

    /**
     * Executes the specified [listener] when a player enters the related [Area].
     */
    fun entrance(listener: AreaListener) {
        this.entrance = listener
    }

    /**
     * Executes the specified [listener] when a player moves around inside the related [Area].
     */
    fun inside(listener: AreaListener) {
        this.inside = listener
    }

    /**
     * Executes the specified [listener] when a player moves exits the related [Area].
     */
    fun exit(listener: AreaListener) {
        this.exit = listener
    }

}