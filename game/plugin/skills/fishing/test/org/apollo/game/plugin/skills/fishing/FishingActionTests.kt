package org.apollo.game.plugin.skills.fishing

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.assertions.startsWith
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.spawnNpc
import org.apollo.game.plugin.testing.junit.api.interactions.spawnObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class FishingActionTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @Test
    fun `Attempting to fish at a spot we don't have the skill to should send the player a message`() {
        val obj = world.spawnObject(1, player.position)

        val option = spyk(FishingSpot.CAGE_HARPOON.option(1))
        val target = FishingTarget(obj.position, option)

        player.startAction(FishingAction(player, target))

        every { option.level } returns Int.MAX_VALUE

        verifyAfter(action.complete()) {
            player.sendMessage(contains("need a fishing level of ${Int.MAX_VALUE}"))
        }
    }

    @Test
    fun `Fishing at a spot we have the skill to should eventually reward fish and experience`() {
        val option = spyk(FishingSpot.CAGE_HARPOON.option(1))
        val obj = world.spawnNpc(FishingSpot.CAGE_HARPOON.npc, player.position)

        val target = spyk(FishingTarget(obj.position, option))
        every { target.isSuccessful(player, any()) } returns true
        every { target.verify(player) } returns true

        player.skillSet.setCurrentLevel(Skill.FISHING, option.level)
        player.startAction(FishingAction(player, target))

        verifyAfter(action.ticks(1)) {
            player.sendMessage(startsWith("You attempt to catch a lobster"))
        }

        after(action.ticks(4)) {
            verify { player.sendMessage(startsWith("You catch a <fish_type>.")) }

            assertTrue(player.inventory.contains(Fish.LOBSTER.id))
            assertEquals(player.skillSet.getExperience(Skill.FISHING), Fish.LOBSTER.experience)
        }
    }

    private companion object {
        @ItemDefinitions
        private val fish = Fish.values()
            .map { ItemDefinition(it.id).apply { name = "<fish_type>" } }

        @ItemDefinitions
        private val tools = FishingTool.values()
            .map { ItemDefinition(it.id).apply { name = "<fish_tool>" } }

        @NpcDefinitions
        private val spots = FishingSpot.values()
            .map { NpcDefinition(it.npc).apply { name = "<fishing_spot>" } }
    }
}