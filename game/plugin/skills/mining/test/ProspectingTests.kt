
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.apollo.game.plugin.testing.verifiers.StringMockkVerifiers.contains
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class ProspectingTests : KotlinPluginTest() {

    init {
        Ore.values()
            .map { ItemDefinition(it.id).also { it.name = "<ore_type>" } }
            .forEach { items[it.id] = it }
    }

    @ParameterizedTest
    @ArgumentsSource(MiningTestDataProvider::class)
    fun `Prospecting a rock should reveal the type of ore it contains`(data: MiningTestData) {
        player.interactWithObject(data.rockId, 2)

        verifyAfter(ticks(1)) { player.sendMessage(contains("examine the rock")) }
        verifyAfter(actionCompleted()) { player.sendMessage(contains("This rock contains <ore_type>")) }
    }

    @ParameterizedTest
    @ArgumentsSource(MiningTestDataProvider::class)
    fun `Prospecting an expired rock should reveal it contains no ore`(data: MiningTestData) {
        player.interactWithObject(data.expiredRockId, 2)

        verifyAfter(actionCompleted()) { player.sendMessage(contains("no ore available in this rock")) }
    }
}