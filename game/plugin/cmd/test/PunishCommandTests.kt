import io.mockk.verify
import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.Name
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class PunishCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var admin: Player

    @TestMock
    @Name("player_two")
    lateinit var player: Player

    @BeforeEach
    fun setup() {
        admin.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
    }

    @Test
    fun `Staff can mute players`() {
        world.commandDispatcher.dispatch(admin, Command("mute", arrayOf("player_two")))

        verify {
            player.setMuted(true)
        }
    }

    @Test
    fun `Staff can unmute players`() {
        player.isMuted = true
        world.commandDispatcher.dispatch(admin, Command("unmute", arrayOf("player_two")))

        verify {
            player.setMuted(false)
        }
    }

    @Test
    fun `Staff cant mute admins`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(admin, Command("unmute", arrayOf("player_two")))

        verify {
            admin.sendMessage("You cannot perform this action on Administrators.")
        }
    }

    @Test
    fun `Cant mute players that arent online`() {
        world.commandDispatcher.dispatch(admin, Command("mute", arrayOf("player555")))

        verify {
            admin.sendMessage("That player does not exist.")
        }
    }

    @Test
    fun `Staff can ban players`() {
        world.commandDispatcher.dispatch(admin, Command("ban", arrayOf("player_two")))

        verify {
            player.ban()
            player.logout()
        }
    }

    @Test
    fun `Staff cant ban admins`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(admin, Command("ban", arrayOf("player_two")))

        verify {
            admin.sendMessage("You cannot perform this action on Administrators.")
        }
    }

    @Test
    fun `Cant ban players that arent online`() {
        world.commandDispatcher.dispatch(admin, Command("ban", arrayOf("player555")))

        verify {
            admin.sendMessage("That player does not exist.")
        }
    }
}