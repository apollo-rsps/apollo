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
class TeleportCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @TestMock
    @Name("player_two")
    lateinit var player_two: Player

    @Test
    fun `Teleport to given coordinates`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("tele", arrayOf("1", "2", "0")))

        assertEquals(Position(1, 2, 0), player.position)
    }

    @Test
    fun `Teleport to given coordinates on players plane when no plane given`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        player.position = Position(1, 1, 1)
        world.commandDispatcher.dispatch(player, Command("tele", arrayOf("1", "2")))

        assertEquals(Position(1, 2, 1), player.position)
    }

    @Test
    fun `Shows current position information`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        player.position = Position(1, 2, 3)
        world.commandDispatcher.dispatch(player, Command("pos", emptyArray()))

        verify {
            player.sendMessage(contains("1, 2, 3"))
        }
    }

    @Test
    fun `Shows another players current position information`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        player_two.position = Position(1, 2, 3)
        world.commandDispatcher.dispatch(player, Command("pos", arrayOf("player_two")))

        verify {
            player.sendMessage(contains("1, 2, 3"))
        }
    }

    @Test
    fun `Shows no position information for a nonexistent player`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("pos", arrayOf("player999")))

        verify {
            player.sendMessage(contains("offline"))
        }
    }

    @ParameterizedTest(name = "::tele {0}")
    @ValueSource(strings = [
        "1 2 <garbage>",
        "1 <garbage> 2",
        "1",
        "1 2 3 4"
    ])
    fun `Help message sent on invalid syntax`(args: String) {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("tele", args.split(" ").toTypedArray()))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }
}