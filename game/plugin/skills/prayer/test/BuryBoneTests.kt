
import BuryBoneAction.Companion.BURY_BONE_ANIMATION
import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.prayer
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.startsWith
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWithItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(ApolloTestingExtension::class)
class BuryBoneTests {

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var action: ActionCapture

    @ParameterizedTest
    @EnumSource(value = Bone::class)
    fun `Burying a bone should send a message`(bone: Bone) {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        verifyAfter(action.ticks(1), "message is sent") {
            player.sendMessage(startsWith("You dig a hole"))
        }
    }

    @ParameterizedTest
    @EnumSource(value = Bone::class)
    fun `Burying a bone should play an animation`(bone: Bone) {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        verifyAfter(action.ticks(1), "animation is played") {
            player.playAnimation(eq(BURY_BONE_ANIMATION))
        }
    }

    @ParameterizedTest
    @EnumSource(value = Bone::class)
    fun `Burying a bone should give the player experience`(bone: Bone) {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        action.ticks(1)

        after(action.complete(), "experience is granted after bone burial") {
            verify { player.sendMessage(startsWith("You bury the bones")) }

            assertEquals(bone.xp, player.prayer.experience)
            assertEquals(player.inventory.getAmount(bone.id), 0)
        }
    }

    private companion object {
        @ItemDefinitions
        fun bones(): Collection<ItemDefinition> {
            return Bone.values().map { ItemDefinition(it.id) }
        }
    }
}