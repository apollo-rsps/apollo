package org.apollo.game.plugin.api

import org.apollo.game.model.Position
import org.apollo.game.plugin.api.Position.component1
import org.apollo.game.plugin.api.Position.component2
import org.apollo.game.plugin.api.Position.component3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionTests {

    @Test
    fun `Positions are destructured in the correct order`() {
        val x = 10
        val y = 20
        val z = 1

        val position = Position(x, y, z)
        val (x2, y2, z2) = position

        assertEquals(x, x2) { "x coordinate mismatch in Position destructuring." }
        assertEquals(y, y2) { "y coordinate mismatch in Position destructuring." }
        assertEquals(z, z2) { "z coordinate mismatch in Position destructuring." }
    }
}