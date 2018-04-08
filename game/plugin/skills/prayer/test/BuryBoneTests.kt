import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.verifyAfter
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.interactions.interactWithItem
import org.apollo.game.plugin.testing.verifiers.StringMockkVerifiers.contains
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


@ExtendWith(ApolloTestingExtension::class)
class BuryBoneTests {

    @ItemDefinitions
    fun bones(): Collection<ItemDefinition> {
        return Bone.values()
            .map { ItemDefinition(it.id) }
    }

    @ParameterizedTest
    @EnumSource(value = Bone::class)
    fun messageWhenBuryingBones(bone: Bone, player: Player, action: ActionCapture) {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        verifyAfter(action.ticks(1), "message after animation") { player.sendMessage(contains("You dig a hole")) }
        after(action.complete(), "experience after completion") {
            verify { player.sendMessage(contains("You bury the bones")) }
            assertEquals(bone.xp, player.skillSet.getExperience(Skill.PRAYER))
            assertEquals(player.inventory.getAmount(bone.id), 0)
        }
    }
}