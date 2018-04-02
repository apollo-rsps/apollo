import org.apollo.game.plugin.skills.mining.Ore

data class MiningTestData(val rockId: Int, val expiredRockId: Int, val ore: Ore)

fun miningTestData(): Collection<MiningTestData> = Ore.values()
    .flatMap { ore -> ore.objects.map { MiningTestData(it.key, it.value, ore) } }
    .toList()