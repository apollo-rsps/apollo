import java.util.stream.Stream
import org.apollo.game.plugin.skills.mining.Ore
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

data class MiningTestData(val rockId: Int, val expiredRockId: Int, val ore: Ore)

fun miningTestData(): Collection<MiningTestData> = Ore.values()
    .flatMap { ore -> ore.objects.map { MiningTestData(it.key, it.value, ore) } }
    .toList()

class MiningTestDataProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return miningTestData().map { Arguments { arrayOf(it) } }.stream()
    }
}