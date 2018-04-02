import assertk.assert
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.junit.Test
import org.junit.runners.Parameterized
import org.mockito.Matchers.contains
import org.mockito.Mockito.verify
import org.powermock.modules.junit4.PowerMockRunnerDelegate


@PowerMockRunnerDelegate(Parameterized::class)
class BuryBoneTests(val bone: Bone) : KotlinPluginTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Bone> {
            return Bone.values().toList()
        }
    }

    init {
        items[bone.id] = ItemDefinition(bone.id)
    }

    @Test
    fun `Burying a bone should send a message and give the player experience`() {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        verifyAfter(ticks(1), player) { sendMessage(contains("You dig a hole")) }
        after(actionCompleted()) {
            verify(player).sendMessage(contains("You bury the bones"))
            assert(player).hasExperience(Skill.PRAYER, bone.xp)
            assert(player.inventory).contains(bone.id, 0)
        }
    }
}