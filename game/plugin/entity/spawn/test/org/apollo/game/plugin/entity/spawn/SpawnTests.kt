package org.apollo.game.plugin.entity.spawn

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.*
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.TestMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(ApolloTestingExtension::class)
class SpawnTests {

    @TestMock
    lateinit var world: World

    @BeforeEach
    fun addNewNpcs() {
        var previousSize: Int

        do {
            previousSize = world.npcRepository.size()
            world.pulse()
        } while (previousSize != world.npcRepository.size())
    }

    @MethodSource("npcs spawned by id")
    @ParameterizedTest(name = "spawning {1} at {2} (id = {0})")
    fun `spawned npcs are in the correct position`(id: Int, name: String, spawn: Position) {
        val npc = world.npcRepository.find { it.id == id }

        assertEquals(spawn, npc?.position) { "Failed to find npc $name with id $id." }
    }

    @MethodSource("npcs spawned by id with directions")
    @ParameterizedTest(name = "spawning {1} with direction {3} (id = {0})")
    fun `spawned npcs are facing the correct direction`(id: Int, name: String, spawn: Position, direction: Direction) {
        val npc = requireNotNull(world.npcRepository.find { it.id == id }) { "Failed to find npc $name with id $id." }
        val facing = spawn.step(1, direction)

        assertEquals(facing, npc.facingPosition)
    }

    @Disabled("Currently no way to test if the animation was played")
    @MethodSource("npcs spawned by id with animations")
    @ParameterizedTest(name = "spawning {1} with animation {3} (id = {0})")
    fun `spawned npcs are playing the correct animation`(id: Int, name: String, spawn: Position, animation: Animation) {
        val npc = requireNotNull(world.npcRepository.find { it.id == id }) { "Failed to find npc $name with id $id." }

        TODO("How to verify that npc.playAnimation was called with $animation.")
    }

    @Disabled("Currently no way to test if the graphic was played")
    @MethodSource("npcs spawned by id with graphics")
    @ParameterizedTest(name = "spawning {1} with graphic {3} (id = {0})")
    fun `spawned npcs are playing the correct graphic`(id: Int, name: String, spawn: Position, graphic: Graphic) {
        val npc = requireNotNull(world.npcRepository.find { it.id == id }) { "Failed to find npc $name with id $id." }

        TODO("How to verify that npc.playGraphic was called with $graphic.")
    }

    @MethodSource("npcs spawned by name")
    @ParameterizedTest(name = "spawning {0}")
    fun `spawns are looked up by name if the id is unspecified`(name: String) {
        val npc = world.npcRepository.find { it.definition.name === name }!!
        val expectedId = name.substringAfterLast("_").toInt()

        assertEquals(expectedId, npc.id)
    }

    companion object {

        // This test class has multiple (hidden) order dependencies because of the nature of the spawn
        // plugin, where npcs are inserted into the world immediately after world initialisation.
        //
        // All npcs that should be spawned by the test must be passed to `spawnNpc` _before_ the test world
        // is created by the ApolloTestingExtension - which means they must be done inside the initialisation
        // block of this companion object.
        //
        // When npcs are created, however, they look up their NpcDefinition - so all of the definitions must
        // be created (via `@NpcDefinitions`) before the initialisation block is executed.
        //
        // The world must also be pulsed after the spawn plugin executes, so that npcs are registered (i.e. moved
        // out of the queue).

        @JvmStatic
        fun `npcs spawned by id`(): List<Arguments> {
            return npcs.filterNot { it.id == null }
                .map { (id, name, position) -> Arguments.of(id, name, position) }
        }

        @JvmStatic
        fun `npcs spawned by id with directions`(): List<Arguments> {
            return npcs.filterNot { it.id == null }
                .map { (id, name, position, direction) -> Arguments.of(id, name, position, direction) }
        }

        @JvmStatic
        fun `npcs spawned by id with animations`(): List<Arguments> {
            return npcs.filterNot { it.id == null }
                .map { (id, name, position, _, animation) -> Arguments.of(id, name, position, animation) }
        }

        @JvmStatic
        fun `npcs spawned by id with graphics`(): List<Arguments> {
            return npcs.filterNot { it.id == null }
                .map { (id, name, position, _, _, graphic) -> Arguments.of(id, name, position, graphic) }
        }

        @JvmStatic
        fun `npcs spawned by name`(): List<Arguments> {
            return npcs.filter { it.id == null }
                .map { (_, name) -> Arguments.of(name) }
        }

        private val npcs = listOf(
            Spawn(0, "hans", Position(1000, 1000, 0), facing = Direction.NORTH, spawnAnimation = Animation(10, 100)),
            Spawn(1, "man", Position(1000, 1000, 0), facing = Direction.NORTH, spawnGraphic = Graphic(154, 0, 100)),
            Spawn(null, "man_2", Position(1000, 1000, 2), facing = Direction.NORTH),
            Spawn(null, "man_3", Position(1000, 1000, 3), facing = Direction.SOUTH),
            Spawn(6, "fakename123", Position(1500, 1500, 3), facing = Direction.EAST, spawnAnimation = Animation(112)),
            Spawn(12, "fakename123", Position(1500, 1500, 3), facing = Direction.WEST, spawnGraphic = Graphic(964))
        )

        @NpcDefinitions
        val definitions = npcs.map { (id, name) ->
            val definitionId = id ?: name.substringAfterLast("_").toInt()
            NpcDefinition(definitionId).also { it.name = name }
        }

        init { // Must come after NpcDef initialisation, before mocked World initialisation
            for ((id, name, position, direction) in npcs) {
                spawnNpc(name, position.x, position.y, position.height, id, direction)
            }
        }
    }
}
