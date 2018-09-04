import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugins.area.action
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class AreaActionTests {

    @TestMock
    lateinit var player: Player

    @Test
    fun `entrance action is triggered when a player enters the area`() {
        var triggered = false
        val position = Position(3222, 3222)

        action("entrance_test_action", predicate = { it == player.position }) {
            triggered = true
        }

        player.position = position

        assertTrue(triggered) { "entrance_test_action was not triggered." }
    }

    @Test
    fun `inside action is triggered when a player moves inside an area`() {
        player.position = Position(3222, 3222)
        var triggered = false

        action("inside_test_action", x = 3220..3224, y = 3220..3224) {
            triggered = true
        }

        player.position = Position(3223, 3222)

        assertTrue(triggered) { "inside_test_action was not triggered." }
    }

    @Test
    fun `exit action is triggered when a player exits the area`() {
        player.position = Position(3222, 3222)

        var triggered = false

        action("exit_test_action", predicate = { it == player.position }) {
            triggered = true
        }

        player.position = Position(3221, 3221)

        assertTrue(triggered) { "exit_test_action was not triggered." }
    }
}