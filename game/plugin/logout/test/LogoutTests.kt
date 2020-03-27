import io.mockk.verify
import org.apollo.game.message.impl.decode.IfActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class LogoutTests {

    companion object {
        const val LOGOUT_BUTTON_ID = 2458
    }

    @TestMock
    lateinit var player: Player

    @Test
    fun `The player should be logged out when they click the logout button`() {
        player.send(IfActionMessage(LOGOUT_BUTTON_ID, 0, 0, 0, 0))

        verify { player.logout() }
    }
}