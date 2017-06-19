import org.apollo.game.model.Position
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.SkillSet
import org.apollo.game.plugins.testing.KotlinPluginTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Matchers.*
import org.mockito.Mockito.verify

class TrainingDummyTest : KotlinPluginTest() {

    companion object {
        const val DUMMY_ID = 823
        val DUMMY_POSITION = Position(3200, 3230, 0)
    }

    @Test fun `Hitting the training dummy should give the player attack experience`() {
        val ctx = context()
        val dummy = ctx.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = ctx.activePlayer.skillSet
        val attackExp = skills.getExperience(Skill.ATTACK)

        ctx.interactWith(dummy, option = 2)
        ctx.waitForActionCompletion()

        assertTrue("Did not gain exp after hitting dummy", skills.getExperience(Skill.ATTACK) > attackExp)
    }

    @Test fun `The player should stop getting attack experience from the training dummy at level 8`() {
        val ctx = context()

        val dummy = ctx.spawnObject(DUMMY_ID, DUMMY_POSITION)
        val skills = ctx.activePlayer.skillSet
        skills.setMaximumLevel(Skill.ATTACK, 8)
        val attackExp = skills.getExperience(Skill.ATTACK)

        ctx.interactWith(dummy, option = 2)
        ctx.waitForActionCompletion()

        verify(ctx.activePlayer).sendMessage(contains("nothing more you can learn"))
        assertTrue("Attack exp has changed since hitting the dummy", attackExp == skills.getExperience(Skill.ATTACK))
    }

}