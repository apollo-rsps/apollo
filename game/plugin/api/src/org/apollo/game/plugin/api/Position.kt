package org.apollo.game.plugin.api

import org.apollo.game.model.Position

/**
 * Support destructuring a Position into its components.
 */
object Position {
    operator fun Position.component1(): Int = x
    operator fun Position.component2(): Int = y
    operator fun Position.component3(): Int = height
}