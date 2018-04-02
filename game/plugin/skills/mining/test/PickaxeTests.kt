import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Assert
import org.junit.Test
import org.junit.runners.Parameterized
import org.powermock.modules.junit4.PowerMockRunnerDelegate

@PowerMockRunnerDelegate(Parameterized::class)
class PickaxeTests(private val pickaxe: Pickaxe) : KotlinPluginTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = Pickaxe.values()
    }

    init {
        Pickaxe.values()
            .map { ItemDefinition(it.id).apply { isStackable = false } }
            .forEach { items[it.id] = it }
    }

    @Test
    fun `No pickaxe is chosen if none are available`() {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)

        Assert.assertEquals(null, Pickaxe.bestFor(player))
    }

    @Test
    fun `The highest level pickaxe is chosen when available`() {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        Assert.assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

    @Test
    fun `Only pickaxes the player has are chosen`() {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(Pickaxe.BRONZE.id)

        Assert.assertEquals(Pickaxe.BRONZE, Pickaxe.bestFor(player))
    }

    @Test
    fun `Pickaxes can be chosen from equipment as well as inventory`() {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        Assert.assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

    @Test
    fun `Pickaxes with a level requirement higher than the player's are ignored`() {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        Pickaxe.values()
            .filter { it.level > pickaxe.level }
            .forEach { player.inventory.add(it.id) }

        Assert.assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

}