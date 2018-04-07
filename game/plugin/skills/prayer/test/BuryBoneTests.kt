
import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.actionCompleted
import org.apollo.game.plugin.testing.ticks
import org.apollo.game.plugin.testing.verifiers.StringMockkVerifiers.contains
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


class BuryBoneTests() : KotlinPluginTest() {

    init {
        Bone.values()
            .map { ItemDefinition(it.id) }
            .forEach { items[it.id] = it }
    }

    @ParameterizedTest
    @EnumSource(value = Bone::class)
    fun `Burying a bone should send a message and give the player experience`(bone: Bone) {
        player.inventory.add(bone.id)
        player.interactWithItem(bone.id, option = 1)

        verifyAfter(ticks(1)) { player.sendMessage(contains("You dig a hole")) }
        after(actionCompleted()) {
            verify { player.sendMessage(contains("You bury the bones")) }
            assertEquals(bone.xp, player.skillSet.getExperience(Skill.PRAYER))
            assertEquals(player.inventory.getAmount(bone.id), 0)
        }
    }
}