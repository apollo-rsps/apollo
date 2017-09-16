import org.apollo.game.model.Position
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Test
import org.mockito.Mockito.verify

class OpenBankTest() : KotlinPluginTest() {

    companion object {
        const val BANK_BOOTH_ID = 2213
        const val BANK_TELLER_ID = 166

        val BANK_POSITION = Position(3200, 3200, 0)
    }

    @Test
    fun `Interacting with a bank teller should open the players bank`() {
        val bankTeller = world.spawnNpc(BANK_TELLER_ID, BANK_POSITION)

        // @todo - these option numbers only match by coincidence, we should be looking up the correct ones
        player.interactWith(bankTeller, option = 2)
        player.waitForActionCompletion()

        verify(player).openBank()
    }

    @Test
    fun `Interacting with a bank booth object should open the players bank`() {
        val bankBooth = world.spawnObject(BANK_BOOTH_ID, BANK_POSITION)

        player.interactWith(bankBooth, option = 2)
        player.waitForActionCompletion()

        verify(player).openBank()
    }

}