
import io.mockk.verify
import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Test

class LogoutTests : KotlinPluginTest() {

	companion object {
		const val LOGOUT_BUTTON_ID = 2458
	}

	@Test fun `The player should be logged out when they click the logout button`() {
		player.notify(ButtonMessage(LOGOUT_BUTTON_ID))

        verify { player.logout() }
	}

}