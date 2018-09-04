import io.mockk.verify
import org.apollo.game.message.impl.ButtonMessage
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
        player.send(ButtonMessage(LOGOUT_BUTTON_ID))

        verify { player.logout() }
    }
}