import io.mockk.verify
import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class BroadcastCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @Test
    fun `Shows basic information on an item`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("broadcast", arrayOf("msg")))

        verify {
            player.sendMessage("[Broadcast] Test: msg")
        }
    }
}