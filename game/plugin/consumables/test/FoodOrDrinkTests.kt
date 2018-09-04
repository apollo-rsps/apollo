package org.apollo.plugin.consumables

import io.mockk.verify
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWithItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodOrDrinkTests {

    companion object {
        const val TEST_FOOD_NAME = "test_food"
        const val TEST_FOOD_ID = 2000
        const val TEST_FOOD_RESTORATION = 5

        const val TEST_DRINK_NAME = "test_drink"
        const val TEST_DRINK_ID = 2001
        const val TEST_DRINK_RESTORATION = 5

        const val HP_LEVEL = 5
        const val MAX_HP_LEVEL = 10
    }

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @BeforeEach
    fun setup() {
        val skills = player.skillSet
        skills.setCurrentLevel(Skill.HITPOINTS, HP_LEVEL)
        skills.setMaximumLevel(Skill.HITPOINTS, MAX_HP_LEVEL)

        food("test_food", TEST_FOOD_ID, TEST_FOOD_RESTORATION)
        drink("test_drink", TEST_DRINK_ID, TEST_DRINK_RESTORATION)
    }

    @Test
    fun `Consuming food or drink should restore the players hitpoints`() {
        val expectedHpLevel = TEST_FOOD_RESTORATION + HP_LEVEL

        player.interactWithItem(TEST_FOOD_ID, option = 1, slot = 1)

        after(action.complete()) {
            assertEquals(expectedHpLevel, player.skillSet.getCurrentLevel(Skill.HITPOINTS))
        }
    }

    @Test
    fun `A message should be sent notifying the player if the item restored hitpoints`() {
        player.interactWithItem(TEST_FOOD_ID, option = 1, slot = 1)

        verifyAfter(action.complete()) { player.sendMessage("It heals some health.") }
    }

    @Test
    fun `A message should not be sent to the player if the item did not restore hitpoints`() {
        player.skillSet.setCurrentLevel(Skill.HITPOINTS, MAX_HP_LEVEL)
        player.interactWithItem(TEST_FOOD_ID, option = 1, slot = 1)

        after(action.complete()) {
            verify(exactly = 0) { player.sendMessage(contains("it heals some")) }
        }
    }

    @Test
    fun `A message should be sent saying the player has drank an item when consuming a drink`() {
        player.interactWithItem(TEST_DRINK_ID, option = 1, slot = 1)

        verifyAfter(action.complete()) { player.sendMessage("You drink the $TEST_DRINK_NAME.") }
    }

    @Test
    fun `A message should be sent saying the player has eaten an item when consuming food`() {
        player.interactWithItem(TEST_FOOD_ID, option = 1, slot = 1)

        verifyAfter(action.complete()) { player.sendMessage("You eat the $TEST_FOOD_NAME.") }
    }
}