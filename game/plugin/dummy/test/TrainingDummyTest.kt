import io.mockk.verify
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.apollo.game.plugin.testing.verifiers.StringMockkVerifiers.contains
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TrainingDummyTest : KotlinPluginTest() {

    companion object {
        const val DUMMY_ID = 823
        val DUMMY_POSITION = Position(3200, 3230, 0)
    }

    @Test
    fun `Hitting the training dummy should give the player attack experience`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)
        player.waitForActionCompletion()

        val afterExp = skills.getExperience(Skill.ATTACK)
        assertThat(afterExp).isGreaterThan(beforeExp)
    }

    @Test
    fun `The player should stop getting attack experience from the training dummy at level 8`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        skills.setMaximumLevel(Skill.ATTACK, 8)
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)
        player.waitForActionCompletion()

        val afterExp = skills.getExperience(Skill.ATTACK)

        verify { player.sendMessage(contains("nothing more you can learn")) }
        assertEquals(afterExp, beforeExp)
    }

}
