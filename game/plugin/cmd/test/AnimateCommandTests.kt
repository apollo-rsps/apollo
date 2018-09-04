import io.mockk.verify
import org.apollo.game.command.Command
import org.apollo.game.model.Animation
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class AnimateCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @Test
    fun `Plays the animation provided as input`() {
        player.privilegeLevel = PrivilegeLevel.MODERATOR
        world.commandDispatcher.dispatch(player, Command("animate", arrayOf("1")))

        verify { player.playAnimation(Animation(1)) }
    }

    @Test
    fun `Help message sent on invalid syntax`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("animate", arrayOf("<garbage>")))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }
}