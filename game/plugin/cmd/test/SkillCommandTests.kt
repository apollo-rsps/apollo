import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class SkillCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @Test
    fun `Max stats to 99`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("max", emptyArray()))

        for (stat in 0 until Skill.RUNECRAFT) {
            assertEquals(99, player.skillSet.getCurrentLevel(stat))
        }
    }
}