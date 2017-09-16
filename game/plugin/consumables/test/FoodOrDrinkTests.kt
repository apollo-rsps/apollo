package org.apollo.plugin.consumables

import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.mockito.KotlinMockitoExtensions.matches
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class FoodOrDrinkTests : KotlinPluginTest() {

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

    @Before override fun setup() {
        super.setup()

        val skills = player.skillSet
        skills.setCurrentLevel(Skill.HITPOINTS, HP_LEVEL)
        skills.setMaximumLevel(Skill.HITPOINTS, MAX_HP_LEVEL)

        food("test_food", TEST_FOOD_ID, TEST_FOOD_RESTORATION)
        drink("test_drink", TEST_DRINK_ID, TEST_DRINK_RESTORATION)
    }

    @Test fun `Consuming food or drink should restore the players hitpoints`() {
        val expectedHpLevel = TEST_FOOD_RESTORATION + HP_LEVEL

        player.notify(ItemOptionMessage(1, -1, TEST_FOOD_ID, 1))
        player.waitForActionCompletion()

        val currentHpLevel = player.skillSet.getCurrentLevel(Skill.HITPOINTS)
        assertThat(currentHpLevel).isEqualTo(expectedHpLevel)
    }

    @Test fun `A message should be sent notifying the player if the item restored hitpoints`() {
        player.notify(ItemOptionMessage(1, -1, TEST_FOOD_ID, 1))
        player.waitForActionCompletion()

        verify(player).sendMessage(matches {
            assertThat(this).contains("heals some health")
        })
    }

    @Test fun `A message should not be sent to the player if the item did not restore hitpoints`() {
        player.skillSet.setCurrentLevel(Skill.HITPOINTS, MAX_HP_LEVEL)
        player.notify(ItemOptionMessage(1, -1, TEST_FOOD_ID, 1))
        player.waitForActionCompletion()

        verify(player, never()).sendMessage(matches {
            assertThat(this).contains("heals some health")
        })
    }

    @Test fun `A message should be sent saying the player has drank an item when consuming a drink`() {
        player.notify(ItemOptionMessage(1, -1, TEST_DRINK_ID, 1))
        player.waitForActionCompletion()

        verify(player).sendMessage(matches {
            assertThat(this).contains("You drink the ${TEST_DRINK_NAME}")
        })
    }

    @Test fun `A message should be sent saying the player has eaten an item when consuming food`() {
        player.notify(ItemOptionMessage(1, -1, TEST_FOOD_ID, 1))
        player.waitForActionCompletion()

        verify(player).sendMessage(matches {
            assertThat(this).contains("You eat the ${TEST_FOOD_NAME}")
        })
    }

}