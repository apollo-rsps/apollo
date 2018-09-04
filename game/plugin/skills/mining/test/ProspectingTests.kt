
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWithObject
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@ExtendWith(ApolloTestingExtension::class)
class ProspectingTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @ParameterizedTest
    @ArgumentsSource(MiningTestDataProvider::class)
    fun `Prospecting a rock should reveal the type of ore it contains`(data: MiningTestData) {
        player.interactWithObject(data.rockId, 2)

        verifyAfter(action.ticks(1)) { player.sendMessage(contains("examine the rock")) }
        verifyAfter(action.complete()) { player.sendMessage(contains("This rock contains <ore_type>")) }
    }

    @ParameterizedTest
    @ArgumentsSource(MiningTestDataProvider::class)
    fun `Prospecting an expired rock should reveal it contains no ore`(data: MiningTestData) {
        player.interactWithObject(data.expiredRockId, 2)

        verifyAfter(action.complete()) { player.sendMessage(contains("no ore available in this rock")) }
    }

    private companion object {
        @ItemDefinitions
        fun ores() = Ore.values().map {
            ItemDefinition(it.id).also { it.name = "<ore_type>" }
        }
    }
}