package org.apollo.game.plugins.area

import org.apollo.game.model.Position
import org.apollo.game.plugin.api.Position.component1
import org.apollo.game.plugin.api.Position.component2
import org.apollo.game.plugin.api.Position.component3

/**
 * An area in the game world.
 */
interface Area {

    /**
     * Returns whether or not the specified [Position] is inside this [Area].
     */
    operator fun contains(position: Position): Boolean
}

internal class RectangularArea(private val x: IntRange, private val y: IntRange, private val height: Int) : Area {

    override operator fun contains(position: Position): Boolean {
        val (x, y, z) = position
        return x in this.x && y in this.y && z == height
    }
}
