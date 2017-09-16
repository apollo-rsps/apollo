import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class LogoutTests : KotlinPluginTest() {

	companion object {
		const val LOGOUT_BUTTON_ID = 2458
	}

	@Test fun `The player should be logged out when they click the logout button`() {
		player.notify(ButtonMessage(LOGOUT_BUTTON_ID))

		verify(player, times(1)).logout()
	}

}