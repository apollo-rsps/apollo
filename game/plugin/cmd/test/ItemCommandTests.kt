import io.mockk.verify
import org.apollo.cache.def.ItemDefinition
import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.contains
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(ApolloTestingExtension::class)
class ItemCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @Test
    fun `Defaults to an amount of 1`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("item", arrayOf("1")))

        assertEquals(1, player.inventory.getAmount(1))
    }

    @Test
    fun `Adds item of specified amount to inventory`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("item", arrayOf("1", "10")))

        assertEquals(10, player.inventory.getAmount(1))
    }

    @ParameterizedTest(name = "::item {0}")
    @ValueSource(strings = ["<garbage>", "1 <garbage>", "<garbage> 1"])
    fun `Help message sent on invalid syntax`(args: String) {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("item", args.split(" ").toTypedArray()))

        verify {
            player.sendMessage(contains("Invalid syntax"))
        }
    }

    companion object {
        @ItemDefinitions
        val items = listOf(ItemDefinition(1))
    }
}