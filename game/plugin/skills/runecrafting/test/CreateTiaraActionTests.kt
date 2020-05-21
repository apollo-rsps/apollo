package org.apollo.game.plugin.skill.runecrafting

import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class CreateTiaraActionTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @Test
    fun `A tiara should be rewarded after action completion`() {
        player.inventory.add(blankTiaraId)
        player.startAction(CreateTiaraAction(player, player.position, Tiara.AIR_TIARA, Altar.AIR_ALTAR))

        after(action.complete(), "tiara added to inventory") {
            assertEquals(1, player.inventory.getAmount(Tiara.AIR_TIARA.id))
        }
    }

    @Test
    fun `Tiaras can only be enchanted on compatible altars`() {
        player.inventory.add(blankTiaraId)
        player.startAction(CreateTiaraAction(player, player.position, Tiara.AIR_TIARA, Altar.BODY_ALTAR))

        verifyAfter(action.complete(), "error message sent") {
            player.sendMessage("You can't use that talisman on this altar.")
        }
    }

    @Test
    fun `Experience is rewarded for enchanting tiaras`() {
        player.inventory.add(blankTiaraId)
        player.skillSet.setExperience(Skill.RUNECRAFT, 0.0)
        player.startAction(CreateTiaraAction(player, player.position, Tiara.AIR_TIARA, Altar.AIR_ALTAR))

        after(action.complete(), "experience gained") {
            assertEquals(Tiara.AIR_TIARA.xp, player.skillSet.getExperience(Skill.RUNECRAFT))
        }
    }

    companion object {
        @ItemDefinitions
        private val tiaras = Tiara.values()
            .map { ItemDefinition(it.id).apply { name = "<tiara>" } }

        @ItemDefinitions
        private val blankTiara = listOf<ItemDefinition>(ItemDefinition(blankTiaraId))
    }
}