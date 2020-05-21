package org.apollo.game.plugin.skill.runecrafting

import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class RunecraftingActionTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @BeforeEach
    fun setupPlayer() {
        player.position = TEST_ALTAR.center
        player.inventory.add(runeEssenceId, 25)
    }

    @Test
    fun `Bonus runes are rewarded depending on the multiplier returned by the rune type`() {
        player.startAction(RunecraftingAction(player, `rune with 1xp and bonus multiplier of 2`, TEST_ALTAR))

        after(action.complete()) {
            assertEquals(50, player.inventory.getAmount(1))
        }
    }

    @Test
    fun `Experience does not stack with bonus multiplier`() {
        player.skillSet.setExperience(Skill.RUNECRAFT, 0.0)
        player.startAction(RunecraftingAction(player, `rune with 1xp and bonus multiplier of 2`, TEST_ALTAR))

        after(action.complete()) {
            assertEquals(25.0, player.skillSet.getExperience(Skill.RUNECRAFT))
        }
    }

    @Test
    fun `Experience is rewarded for each rune essence used`() {
        player.skillSet.setExperience(Skill.RUNECRAFT, 0.0)
        player.startAction(RunecraftingAction(player, `rune with 1xp and bonus multiplier of 1`, TEST_ALTAR))

        after(action.complete()) {
            assertEquals(25.0, player.skillSet.getExperience(Skill.RUNECRAFT))
        }
    }

    @Test
    fun `Cannot create runes that are too high of a level`() {
        player.skillSet.setCurrentLevel(Skill.RUNECRAFT, 1)
        player.startAction(RunecraftingAction(player, `rune with required level of 99`, TEST_ALTAR))

        after(action.complete()) {
            verify { player.sendMessage("You need a runecrafting level of 99 to craft this rune.") }

            assertEquals(25, player.inventory.getAmount(runeEssenceId))
            assertEquals(0, player.inventory.getAmount(1))
        }
    }

    companion object {
        val TEST_ALTAR = Altar.AIR_ALTAR

        val `rune with required level of 99` = object : Rune {
            override val id = 1
            override val altar: Altar = TEST_ALTAR
            override val level = 99
            override val xp = 1.0

            override fun getBonusMultiplier(playerLevel: Int) = 1.0
        }

        val `rune with 1xp and bonus multiplier of 1` = object : Rune {
            override val id = 1
            override val altar: Altar = TEST_ALTAR
            override val level = 1
            override val xp = 1.0

            override fun getBonusMultiplier(playerLevel: Int) = 1.0
        }

        val `rune with 1xp and bonus multiplier of 2` = object : Rune {
            override val id = 1
            override val altar: Altar = TEST_ALTAR
            override val level = 1
            override val xp = 1.0

            override fun getBonusMultiplier(playerLevel: Int) = 2.0
        }

        @ItemDefinitions
        private val runeEssence = listOf(ItemDefinition(runeEssenceId).apply {
            isStackable = true
        })

        @ItemDefinitions
        private val runes = listOf(ItemDefinition(1).apply {
            name = "<rune_name>"
            isStackable = true
        })
    }
}