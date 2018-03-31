import org.apollo.cache.def.ItemDefinition
import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.PluginContext
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.mockito.KotlinMockitoExtensions.matches
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runners.Parameterized
import org.mockito.Matchers.anyInt
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunnerDelegate


@PowerMockRunnerDelegate(Parameterized::class)
@PrepareForTest(World::class, PluginContext::class, Player::class, ItemDefinition::class)
class BuryBoneTests(val bone: Bone) : KotlinPluginTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Bone> {
            return Bone.values().toList()
        }
    }

    @Before
    override fun setup() {
        super.setup()

        mockStatic(ItemDefinition::class.java)

        `when`(ItemDefinition.lookup(anyInt())).thenAnswer {
            val args = it.arguments
            val def = ItemDefinition(args[0] as Int)

            def
        }
    }

    @Test
    fun `Burying a bone should send a message and give the player experience`() {
        val startPrayerExperience = player.skillSet.getExperience(Skill.PRAYER)

        player.inventory.add(bone.id)
        player.notify(ItemOptionMessage(1, -1, bone.id, player.inventory.slotOf(bone.id)))
        player.waitForActionCompletion()

        verify(player).sendMessage(matches {
            assertThat(this).contains("You dig a hole in the ground")
        })

        verify(player).sendMessage(matches {
            assertThat(this).contains("bury the bones")
        })

        assertThat(player.skillSet.getExperience(Skill.PRAYER)).isEqualTo(startPrayerExperience + bone.xp)
    }
}