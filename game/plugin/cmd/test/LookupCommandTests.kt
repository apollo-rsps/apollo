import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.command.Command
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.setting.PrivilegeLevel
import org.apollo.game.plugin.testing.assertions.startsWith
import org.apollo.game.plugin.testing.assertions.strEq
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.ObjectDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(ApolloTestingExtension::class)
class LookupCommandTests {

    @TestMock
    lateinit var world: World

    @TestMock
    lateinit var player: Player

    @Test
    fun `Shows basic information on an item`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("iteminfo", arrayOf("1")))

        verify {
            player.sendMessage("Item 1 is called <item_name> and is not members only.")
            player.sendMessage("Its description is `<description>`.")
        }
    }

    @Test
    fun `Shows basic information on an npc`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("npcinfo", arrayOf("1")))

        verify {
            player.sendMessage("Npc 1 is called <npc_name> and has a combat level of 126.")
            player.sendMessage("Its description is `<description>`.")
        }
    }

    @Test
    fun `Shows basic information on an object`() {
        player.privilegeLevel = PrivilegeLevel.ADMINISTRATOR
        world.commandDispatcher.dispatch(player, Command("objectinfo", arrayOf("1")))

        verify {
            player.sendMessage("Object 1 is called <object_name> (width=1, length=1).")
            player.sendMessage("Its description is `<description>`.")
        }
    }

    companion object {
        @ItemDefinitions
        val items = listOf(ItemDefinition(1).apply {
            name = "<item_name>"
            description = "<description>"
            isMembersOnly = false
        })

        @NpcDefinitions
        val npcs = listOf(NpcDefinition(1).apply {
            name = "<npc_name>"
            combatLevel = 126
            description = "<description>"
        })

        @ObjectDefinitions
        val objects = listOf(ObjectDefinition(1).apply {
            name = "<object_name>"
            description = "<description>"
            width = 1
            length = 1
        })
    }

}