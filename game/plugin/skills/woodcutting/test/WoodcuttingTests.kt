
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Random
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWithObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@ExtendWith(ApolloTestingExtension::class)
class WoodcuttingTests {

    @TestMock
    lateinit var action: ActionCapture

    @TestMock
    lateinit var player: Player

    @ParameterizedTest
    @ArgumentsSource(WoodcuttingTestDataProvider::class)
    fun `Attempting to cut a tree when the player has no axe should send a message`(data: WoodcuttingTestData) {
        player.interactWithObject(data.treeId, 1)

        verify { player.sendMessage(contains("do not have an axe")) }
    }

    @ParameterizedTest
    @ArgumentsSource(WoodcuttingTestDataProvider::class)
    fun `Attempting to cut a tree when the player is too low levelled should send a message`(data: WoodcuttingTestData) {
        assumeTrue(data.tree.level > 1, "Normal trees are covered by axe requirements")

        player.inventory.add(Axe.BRONZE.id)
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, data.tree.level - 1)

        player.interactWithObject(data.treeId, 1)

        verifyAfter(action.complete()) { player.sendMessage(contains("do not have the required level")) }
    }

    @Disabled("Mocking constructors is not supported in mockk.  Update WoodcuttingAction to pass a chance value")
    @ParameterizedTest
    @ArgumentsSource(WoodcuttingTestDataProvider::class)
    fun `Cutting a tree we have the skill to cut should eventually reward logs and experience`(
        data: WoodcuttingTestData
    ) {
        // Mock RNG instances used by mining internally to determine success
        // @todo - improve this so we don't have to mock Random
        val rng = mockk<Random>()
        every { rng.nextInt(100) } answers { 0 }

        player.inventory.add(Axe.BRONZE.id)
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, data.tree.level)

        player.interactWithObject(data.treeId, 1)

        verifyAfter(action.ticks(1)) { player.sendMessage(contains("You swing your axe")) }

        after(action.ticks(Axe.BRONZE.pulses)) {
            // @todo - cummulative ticks() calls?
            verify { player.sendMessage("You manage to cut some <tree_type>") }
            assertEquals(data.tree.exp, player.skillSet.getExperience(Skill.WOODCUTTING))
            assertEquals(1, player.inventory.getAmount(data.tree.id))
        }
    }

    private companion object {
        @ItemDefinitions
        fun logs() = woodcuttingTestData().map {
            ItemDefinition(it.tree.id).also { it.name = "<tree_type>" }
        }

        @ItemDefinitions
        fun tools() = listOf(ItemDefinition(Axe.BRONZE.id))
    }
}