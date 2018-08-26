import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.Item
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.startsWith
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
internal class IdentifyHerbActionTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    private val herb = Herb.GUAM_LEAF

    @BeforeEach
    internal fun startAction() {
        player.inventory.set(0, Item(herb.unidentified))
        player.startAction(IdentifyHerbAction(player, 0, herb))
    }

    @Test
    internal fun `Identifying a herb should send a message if the player doesnt have the required level`() {
        player.skillSet.setCurrentLevel(Skill.HERBLORE, 0)

        verifyAfter(action.complete(), "level requirement message sent to player") {
            player.sendMessage(startsWith("You need a Herblore level of"))
        }
    }

    @Test
    internal fun `Identifying a herb should remove the undentified herb`() {
        after(action.complete()) {
            assertEquals(0, player.inventory.getAmount(herb.unidentified))
        }
    }

    @Test
    internal fun `Identifying a herb should add the identified herb to the players inventory`() {
        after(action.complete()) {
            assertEquals(1, player.inventory.getAmount(herb.identified))
        }
    }

    private companion object {
        @ItemDefinitions
        val identifiedHerbs = Herb.values()
            .map { ItemDefinition(it.identified).apply { name = "<identified_herb>" } }

        @ItemDefinitions
        val unidentifiedHerbs = Herb.values()
            .map { ItemDefinition(it.unidentified).apply { name = "<unidentified_herb>" } }
    }
}