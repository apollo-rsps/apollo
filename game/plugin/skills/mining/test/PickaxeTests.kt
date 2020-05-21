import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.mining.Pickaxe
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(ApolloTestingExtension::class)
class PickaxeTests {

    @TestMock
    lateinit var player: Player

    @ParameterizedTest
    @EnumSource(Pickaxe::class)
    fun `No pickaxe is chosen if none are available`(pickaxe: Pickaxe) {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)

        assertEquals(null, Pickaxe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Pickaxe::class)
    fun `The highest level pickaxe is chosen when available`(pickaxe: Pickaxe) {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Pickaxe::class)
    fun `Only pickaxes the player has are chosen`(pickaxe: Pickaxe) {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(Pickaxe.BRONZE.id)

        assertEquals(Pickaxe.BRONZE, Pickaxe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(value = Pickaxe::class)
    fun `Pickaxes can be chosen from equipment as well as inventory`(pickaxe: Pickaxe) {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(value = Pickaxe::class)
    fun `Pickaxes with a level requirement higher than the player's are ignored`(pickaxe: Pickaxe) {
        player.skillSet.setCurrentLevel(Skill.MINING, pickaxe.level)
        player.inventory.add(pickaxe.id)

        Pickaxe.values()
            .filter { it.level > pickaxe.level }
            .forEach { player.inventory.add(it.id) }

        assertEquals(pickaxe, Pickaxe.bestFor(player))
    }

    private companion object {
        @ItemDefinitions
        fun pickaxes() = Pickaxe.values().map {
            ItemDefinition(it.id).apply { isStackable = false }
        }
    }
}