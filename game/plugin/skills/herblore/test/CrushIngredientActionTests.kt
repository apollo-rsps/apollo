import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
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
internal class CrushIngredientActionTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    private val ingredient = CrushableIngredient.BIRDS_NEST

    @BeforeEach
    internal fun startAction() {
        player.inventory.add(ingredient.uncrushed)
        player.startAction(CrushIngredientAction(player, ingredient))
    }

    @Test
    internal fun `Preparing an uncrushed ingredient rewards a new ingredient after 2 ticks`() {
        after(action.ticks(2), "ingredient removed and new ingredient added") {
            assertEquals(0, player.inventory.getAmount(ingredient.uncrushed))
            assertEquals(1, player.inventory.getAmount(ingredient.id))
        }
    }

    @Test
    internal fun `Preparing an uncrushed ingredient should send a message to the player after 2 ticks`() {
        verifyAfter(action.ticks(2), "notification message sent to the player") {
            player.sendMessage(startsWith("You carefully grind the <unprepared_ingredient> to dust"))
        }
    }

    @Test
    internal fun `Preparing an uncrushed ingredient should play an animation on the first tick`() {
        verifyAfter(action.ticks(1), "grinding animation played") {
            player.playAnimation(match { it.id == 364 })
        }
    }

    private companion object {
        @ItemDefinitions
        private val ingredients = CrushableIngredient.values()
            .map { ItemDefinition(it.uncrushed).apply { name = "<unprepared_ingredient>" } }

        @ItemDefinitions
        private val prepared = CrushableIngredient.values()
            .map { ItemDefinition(it.id).apply { name = "<prepared_ingredient>" } }
    }
}