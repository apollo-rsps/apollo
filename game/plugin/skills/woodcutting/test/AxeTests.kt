import org.apollo.cache.def.ItemDefinition
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.Skill
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(ApolloTestingExtension::class)
class AxeTests {

    @TestMock
    lateinit var player: Player

    @ParameterizedTest
    @EnumSource(Axe::class)
    fun `No axe is chosen if none are available`(axe: Axe) {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)

        assertEquals(null, Axe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Axe::class)
    fun `The highest level axe is chosen when available`(axe: Axe) {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        assertEquals(axe, Axe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Axe::class)
    fun `Only axes the player has are chosen`(axe: Axe) {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(Axe.BRONZE.id)

        assertEquals(Axe.BRONZE, Axe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Axe::class)
    fun `Axes can be chosen from equipment as well as inventory`(axe: Axe) {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        assertEquals(axe, Axe.bestFor(player))
    }

    @ParameterizedTest
    @EnumSource(Axe::class)
    fun `Axes with a level requirement higher than the player's are ignored`(axe: Axe) {
        player.skillSet.setCurrentLevel(Skill.WOODCUTTING, axe.level)
        player.inventory.add(axe.id)

        Axe.values()
            .filter { it.level > axe.level }
            .forEach { player.inventory.add(it.id) }

        assertEquals(axe, Axe.bestFor(player))
    }

    private companion object {
        @ItemDefinitions
        fun axes() = Axe.values().map {
            ItemDefinition(it.id).apply { isStackable = false }
        }
    }
}