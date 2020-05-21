
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWith
import org.apollo.game.plugin.testing.junit.api.interactions.spawnNpc
import org.apollo.game.plugin.testing.junit.api.interactions.spawnObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class OpenBankTest {

    companion object {
        const val BANK_BOOTH_ID = 2213
        const val BANK_TELLER_ID = 166

        val BANK_POSITION = Position(3200, 3200, 0)
    }

    @TestMock
    lateinit var action: ActionCapture

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var world: World

    @Test
    fun `Interacting with a bank teller should open the players bank`() {
        val bankTeller = world.spawnNpc(BANK_TELLER_ID, BANK_POSITION)

        // @todo - these option numbers only match by coincidence, we should be looking up the correct ones
        player.interactWith(bankTeller, option = 2)

        verifyAfter(action.complete()) { player.openBank() }
    }

    @Test
    fun `Interacting with a bank booth object should open the players bank`() {
        val bankBooth = world.spawnObject(BANK_BOOTH_ID, BANK_POSITION)

        player.interactWith(bankBooth, option = 2)

        verifyAfter(action.complete()) { player.openBank() }
    }
}