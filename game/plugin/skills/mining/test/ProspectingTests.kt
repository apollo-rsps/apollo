
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.junit.Test
import org.junit.runners.Parameterized
import org.mockito.Matchers.contains
import org.powermock.modules.junit4.PowerMockRunnerDelegate

@PowerMockRunnerDelegate(Parameterized::class)
class ProspectingTests(private val data: MiningTestData) : KotlinPluginTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = miningTestData()
    }

    init {
        val def = ItemDefinition(data.ore.id)
        def.name = "<ore_type>"

        items[data.ore.id] = def
    }

    @Test
    fun `Prospecting a rock should reveal the type of ore it contains`() {
        player.interactWithObject(data.rockId, 2)

        verifyAfter(ticks(1), player) { sendMessage(contains("You examine the rock")) }
        verifyAfter(actionCompleted(), player) { sendMessage(contains("This rock contains <ore_type>")) }
    }

    @Test
    fun `Prospecting an expired rock should reveal it contains no ore`() {
        player.interactWithObject(data.expiredRockId, 2)

        verifyAfter(actionCompleted(), player) { sendMessage(contains("no ore available in this rock")) }
    }
}