import io.mockk.verify
import org.apollo.game.command.Command
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.Name
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(ApolloTestingExtension::class)
class TeleportToPlayerCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @TestMock
    @Name("player_two")
    lateinit var secondPlayer: Player

    @Test
    fun `Teleport to given player`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        player.position = Position(300, 300)
        secondPlayer.position = Position(1, 2)
        world.commandDispatcher.dispatch(player, Command("teleto", arrayOf("player_two")))

        assertEquals(secondPlayer.position, player.position)
    }

    @ParameterizedTest(name = "::teleto {0}")
    @ValueSource(strings = [
        "<garbage_invalid_playername>"
    ])
    fun `Help message sent on invalid syntax`(args: String) {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("teleto", args.split(" ").toTypedArray()))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }
}