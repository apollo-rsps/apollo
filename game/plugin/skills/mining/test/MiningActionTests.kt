
import io.mockk.every
import io.mockk.spyk
import io.mockk.staticMockk
import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.api.replaceObject
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.skills.mining.TIN_OBJECTS
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.spawnObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class MiningActionTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @Test
    fun `Attempting to mine a rock we don't have the skill to should send the player a message`() {
        val obj = world.spawnObject(1, player.position)
        val target = spyk(MiningTarget(obj.id, obj.position, Ore.TIN))

        every { target.skillRequirementsMet(player) } returns false

        player.startAction(MiningAction(player, Pickaxe.BRONZE, target))

        verifyAfter(action.complete()) {
            player.sendMessage(contains("do not have the required level"))
        }
    }

    @Test
    fun `Mining a rock we have the skill to mine should eventually reward ore and experience`() {
        val (tinId, expiredTinId) = TIN_OBJ_IDS
        val obj = world.spawnObject(tinId, player.position)
        val target = spyk(MiningTarget(obj.id, obj.position, Ore.TIN))
        staticMockk("org.apollo.game.plugin.api.WorldKt").mock()

        every { target.skillRequirementsMet(player) } returns true
        every { target.isSuccessful(player, any()) } returns true
        every { world.replaceObject(obj, any(), any()) } answers { }

        player.skillSet.setCurrentLevel(Skill.MINING, Ore.TIN.level)
        player.startAction(MiningAction(player, Pickaxe.BRONZE, target))

        verifyAfter(action.ticks(1)) {
            player.sendMessage(contains("You swing your pick"))
        }

        after(action.complete()) {
            verify { player.sendMessage("You manage to mine some <ore_type>") }
            verify { world.replaceObject(obj, expiredTinId, Ore.TIN.respawn) }

            assertTrue(player.inventory.contains(Ore.TIN.id))
            assertEquals(player.skillSet.getExperience(Skill.MINING), Ore.TIN.exp)
        }
    }

    private companion object {
        private val TIN_OBJ_IDS = TIN_OBJECTS.entries.first()

        @ItemDefinitions
        fun ores() = Ore.values()
            .map { ItemDefinition(it.id).apply { name = "<ore_type>" } }

        @ItemDefinitions
        fun pickaxes() = listOf(ItemDefinition(Pickaxe.BRONZE.id))
    }
}