import assertk.assert
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.junit.Before
import org.junit.Test
import org.junit.runners.Parameterized
import org.mockito.Matchers.contains
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito.whenNew
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import java.util.*


@PowerMockRunnerDelegate(Parameterized::class)
class MiningTests(val data: MiningTestData) : KotlinPluginTest() {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = miningTestData()
    }

    init {
        items[Pickaxe.BRONZE.id] = ItemDefinition(Pickaxe.BRONZE.id)

        val def = ItemDefinition(data.ore.id)
        def.name = "<ore_type>"

        items[data.ore.id] = def
    }

    @Before
    override fun setup() {
        super.setup()

        player.inventory.add(Pickaxe.BRONZE.id)
    }

    @Test
    fun `Attempting to mine a rock we don't have the skill to should send the player a message`() {
        player.skillSet.setCurrentLevel(Skill.MINING, data.ore.level - 1)
        player.interactWithObject(data.rockId, 1)

        verifyAfter(actionCompleted(), player) { sendMessage(contains("do not have the required level")) }
    }

    @Test
    fun `Mining a rock we have the skill to mine should eventually reward ore and experience`() {
        // Mock RNG instances used by mining internally to determine success
        // @todo - improve this so we don't have to mock Random
        val rng = mock(Random::class.java)
        `when`(rng.nextInt(100)).thenReturn(0)
        whenNew(Random::class.java).withAnyArguments().thenReturn(rng)

        player.skillSet.setCurrentLevel(Skill.MINING, data.ore.level)
        player.interactWithObject(data.rockId, 1)

        verifyAfter(ticks(1), player) { sendMessage(contains("You swing your pick")) }

        // @todo - cummulative ticks() calls?
        after(ticks(2 + Pickaxe.BRONZE.pulses)) {
            verify(player).sendMessage("You manage to mine some <ore_type>")
            assert(player).hasExperience(Skill.MINING, data.ore.exp)
            assert(player.inventory).contains(data.ore.id)
        }
    }
}