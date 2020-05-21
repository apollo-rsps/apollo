package org.apollo.game.plugin.api

import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class PlayerTests {

    @TestMock
    lateinit var player: Player

    @BeforeEach
    fun setHitpointsLevel() {
        player.skillSet.setSkill(Skill.HITPOINTS, Skill(1_154.0, 10, 10))
    }

    @Test
    fun `can boost skill above maximum level`() {
        player.apply {
            hitpoints.boost(5)

            assertEquals(15, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `boosts to the same skill do not accumulate`() {
        player.apply {
            hitpoints.boost(5)
            hitpoints.boost(4)

            assertEquals(15, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `greater boosts can override earlier boosts`() {
        player.apply {
            hitpoints.boost(5)
            hitpoints.boost(7)

            assertEquals(17, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `can drain skills`() {
        player.apply {
            hitpoints.drain(5)

            assertEquals(5, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `repeated drains on the same skill accumulate`() {
        player.apply {
            hitpoints.drain(4)
            hitpoints.drain(5)

            assertEquals(1, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `cannot drain skills below zero`() {
        player.apply {
            hitpoints.drain(99)

            assertEquals(0, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `can restore previously-drained skills`() {
        player.skillSet.setCurrentLevel(Skill.HITPOINTS, 1)

        player.apply {
            hitpoints.restore(5)

            assertEquals(6, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `repeated restores on the same skill accumulate`() {
        player.skillSet.setCurrentLevel(Skill.HITPOINTS, 1)

        player.apply {
            hitpoints.restore(3)
            hitpoints.restore(4)

            assertEquals(8, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }

    @Test
    fun `cannot restore skills above their maximum level`() {
        player.skillSet.setCurrentLevel(Skill.HITPOINTS, 1)

        player.apply {
            hitpoints.restore(99)

            assertEquals(10, hitpoints.current)
            assertEquals(10, hitpoints.maximum)
        }
    }
}