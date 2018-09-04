import io.mockk.verify
import org.apollo.cache.def.NpcDefinition
import org.apollo.game.command.Command
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(ApolloTestingExtension::class)
class SpawnCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @BeforeEach
    fun setup() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
    }

    @Test
    fun `Spawns NPC at players position by default`() {
        player.position = Position(3, 3, 0)
        world.commandDispatcher.dispatch(player, Command("spawn", arrayOf("1")))

        verify {
            world.register(match<Npc> {
                it.id == 1 && it.position == Position(3, 3, 0)
            })
        }
    }

    @Test
    fun `Spawn NPC at given position`() {
        world.commandDispatcher.dispatch(player, Command("spawn", arrayOf("1", "5", "5")))

        verify {
            world.register(match<Npc> {
                it.id == 1 && it.position == Position(5, 5)
            })
        }
    }

    @ParameterizedTest(name = "::spawn {0}")
    @ValueSource(strings = [
        "<garbage>",
        "1 2",
        "1 2 <garbage>",
        "1 2 3 <garbage>"
    ])
    fun `Help message on invalid syntax`(args: String) {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("spawn", args.split(" ").toTypedArray()))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }

    companion object {
        @NpcDefinitions
        val npcs = listOf(NpcDefinition(1))
    }
}