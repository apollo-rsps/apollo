import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Assert
import org.junit.Test
import org.junit.runners.Parameterized
import org.powermock.modules.junit4.PowerMockRunnerDelegate

@PowerMockRunnerDelegate(Parameterized::class)
class AxeTests(private val axe: Axe) : KotlinPluginTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = Axe.values()
    }

    init {
        Axe.values()
            .map { ItemDefinition(it.id).apply { isStackable = false } }
            .forEach { items[it.id] = it }
    }

    @Test
    fun `No axe is chosen if none are available`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)

        Assert.assertEquals(null, Axe.bestFor(player))
    }

    @Test
    fun `The highest level axe is chosen when available`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        Assert.assertEquals(axe, Axe.bestFor(player))
    }

    @Test
    fun `Only axes the player has are chosen`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(Axe.BRONZE.id)

        Assert.assertEquals(Axe.BRONZE, Axe.bestFor(player))
    }

    @Test
    fun `Axes can be chosen from equipment as well as inventory`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        Assert.assertEquals(axe, Axe.bestFor(player))
    }

    @Test
    fun `Axes with a level requirement higher than the player's are ignored`() {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        Axe.values()
            .filter { it.level > axe.level }
            .forEach { player.inventory.add(it.id) }

        Assert.assertEquals(axe, Axe.bestFor(player))
    }

}