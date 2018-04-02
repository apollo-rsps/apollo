import assertk.assert
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.junit.Before
import org.junit.Test
import org.junit.runners.Parameterized
import org.mockito.Matchers.contains
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.whenNew
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import java.util.Random

@PowerMockRunnerDelegate(Parameterized::class)
class WoodcuttingTests(private val data: WoodcuttingTestData) : KotlinPluginTest() {

    init {
        items[Axe.BRONZE.id] = ItemDefinition(Axe.BRONZE.id)

        val def = ItemDefinition(data.tree.id)
        def.name = "<tree_type>"

        items[data.tree.id] = def
    }

    @Before
    override fun setup() {
        super.setup()

        player.inventory.add(Axe.BRONZE.id)
    }

    @Test
    fun `Attempting to cut a tree we don't have the skill to should send the player a message`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, data.tree.level - 1)
        player.interactWithObject(data.treeId, 1)

        verifyAfter(actionCompleted(), player) { sendMessage(contains("do not have the required level")) }
    }

    @Test
    fun `Cutting a tree we have the skill to cut should eventually reward logs and experience`() {
        // Mock RNG instances used by mining internally to determine success
        // @todo - improve this so we don't have to mock Random
        val rng = mock(Random::class.java)
        `when`(rng.nextInt(100)).thenReturn(0)
        whenNew(Random::class.java).withAnyArguments().thenReturn(rng)

        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, data.tree.level)
        player.interactWithObject(data.treeId, 1)

        verifyAfter(ticks(1), player) { sendMessage(contains("You swing your axe")) }

        // @todo - cummulative ticks() calls?
        after(ticks(2 + Axe.BRONZE.pulses)) {
            verify(player).sendMessage("You manage to cut some <tree_type>")
            assert(player).hasExperience(Skill.WOODCUTTING, data.tree.exp)
            assert(player.inventory).contains(data.tree.id)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = woodcuttingTestData()
    }

}