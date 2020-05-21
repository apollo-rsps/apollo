
import io.mockk.verify
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.assertions.after
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.ActionCapture
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.apollo.game.plugin.testing.junit.api.interactions.interactWith
import org.apollo.game.plugin.testing.junit.api.interactions.spawnObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class TrainingDummyTest {

    companion object {
        const val DUMMY_ID = 823
        val DUMMY_POSITION = Position(3200, 3230, 0)
    }

    @TestMock
    lateinit var action: ActionCapture

    @TestMock
    lateinit var player: Player

    @TestMock
    lateinit var world: World

    @Test
    fun `Hitting the training dummy should give the player attack experience`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)

        after(action.complete()) {
            assertTrue(skills.getExperience(Skill.ATTACK) > beforeExp)
        }
    }

    @Test
    fun `The player should stop getting attack experience from the training dummy at level 8`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        skills.setMaximumLevel(Skill.ATTACK, 8)
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)

        after(action.complete()) {
            verify { player.sendMessage(contains("nothing more you can learn")) }
            assertEquals(beforeExp, skills.getExperience(Skill.ATTACK))
        }
    }
}
