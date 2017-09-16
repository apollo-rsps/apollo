import org.apollo.game.model.Position
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.contains
import org.mockito.Mockito.verify

class TrainingDummyTest : KotlinPluginTest() {

    companion object {
        const val DUMMY_ID = 823
        val DUMMY_POSITION = Position(3200, 3230, 0)
    }

    @Test fun `Hitting the training dummy should give the player attack experience`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)
        player.waitForActionCompletion()

        val afterExp = skills.getExperience(Skill.ATTACK)
        assertThat(afterExp).isGreaterThan(beforeExp)
    }

    @Test fun `The player should stop getting attack experience from the training dummy at level 8`() {
        val dummy = world.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = player.skillSet
        skills.setMaximumLevel(Skill.ATTACK, 8)
        val beforeExp = skills.getExperience(Skill.ATTACK)

        player.interactWith(dummy, option = 2)
        player.waitForActionCompletion()

        val afterExp = skills.getExperience(Skill.ATTACK)

        verify(player).sendMessage(contains("nothing more you can learn"))
        assertThat(afterExp).isEqualTo(beforeExp)
    }

}
