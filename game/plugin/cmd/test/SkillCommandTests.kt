import io.mockk.verify
import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(ApolloTestingExtension::class)
class SkillCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @BeforeEach
    fun setup() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
    }

    @Test
    fun `Max stats to 99`() {
        world.commandDispatcher.dispatch(player, Command("max", emptyArray()))

        for (stat in 0 until Skill.RUNECRAFT) {
            assertEquals(99, player.skillSet.getCurrentLevel(stat))
        }
    }

    @Test
    fun `Set skill to given level`() {
        world.commandDispatcher.dispatch(player, Command("level", arrayOf("1", "99")))

        assertEquals(99, player.skillSet.getCurrentLevel(1))
    }

    @Test
    fun `Set skill to given experience`() {
        world.commandDispatcher.dispatch(player, Command("xp", arrayOf("1", "50")))

        assertEquals(50.0, player.skillSet.getExperience(1))
    }

    @ParameterizedTest(name = "::{0}")
    @ValueSource(strings = [
        "level 50 100",
        "level 15 100",
        "level <garbage> 100",
        "level 15 <garbage>",
        "level",
        "xp",
        "xp 50 <garbage>",
        "xp <garbage> 50"
    ])
    fun `Help message shown on invalid syntax`(command: String) {
        val args = command.split(" ").toMutableList()
        val cmd = args.removeAt(0)

        world.commandDispatcher.dispatch(player, Command(cmd, args.toTypedArray()))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }
}