package org.apollo.game.plugin.api

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.apollo.game.plugin.testing.junit.ApolloTestingExtension
import org.apollo.game.plugin.testing.junit.api.annotations.ItemDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.NpcDefinitions
import org.apollo.game.plugin.testing.junit.api.annotations.ObjectDefinitions
import org.apollo.game.plugin.testing.junit.params.ItemDefinitionSource
import org.apollo.game.plugin.testing.junit.params.NpcDefinitionSource
import org.apollo.game.plugin.testing.junit.params.ObjectDefinitionSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest

@ExtendWith(ApolloTestingExtension::class)
class DefinitionsTests {

    @Test
    fun `can find an ItemDefinition directly using its id`() {
        val searched = Definitions.item(0)
        assertEquals(items.first().id, searched.id)
    }

    @Test
    fun `can find an ItemDefinition using its name`() {
        val searched = Definitions.item("item_two")
        assertEquals(items[2].id, searched?.id)
    }

    @ParameterizedTest
    @ItemDefinitionSource
    fun `can find ItemDefinitions directly using id suffixing`(item: ItemDefinition) {
        val searched = Definitions.item("${item.name}_${item.id}")
        assertEquals(item.id, searched?.id)
    }

    @Test
    fun `can find an NpcDefinition directly using its id`() {
        val searched = Definitions.npc(0)
        assertEquals(npcs.first().id, searched.id)
    }

    @Test
    fun `can find an NpcDefinition using its name`() {
        val searched = Definitions.npc("npc_two")
        assertEquals(items[2].id, searched?.id)
    }

    @ParameterizedTest
    @NpcDefinitionSource
    fun `can find NpcDefinitions directly using id suffixing`(npc: NpcDefinition) {
        val searched = Definitions.npc("${npc.name}_${npc.id}")
        assertEquals(npc.id, searched?.id)
    }

    @Test
    fun `can find an ObjectDefinition directly using its id`() {
        val searched = Definitions.obj(0)
        assertEquals(objs.first().id, searched.id)
    }

    @Test
    fun `can find an ObjectDefinition using its name`() {
        val searched = Definitions.obj("obj_two")
        assertEquals(items[2].id, searched?.id)
    }

    @ParameterizedTest
    @ObjectDefinitionSource
    fun `can find ObjectDefinitions directly using id suffixing`(obj: ObjectDefinition) {
        val searched = Definitions.obj("${obj.name}_${obj.id}")
        assertEquals(obj.id, searched?.id)
    }

    private companion object {

        @ItemDefinitions
        val items = listOf("item zero", "item one", "item two", "item duplicate name", "item duplicate name")
            .mapIndexed { id, name -> ItemDefinition(id).also { it.name = name } }

        @NpcDefinitions
        val npcs = listOf("npc zero", "npc one", "npc two", "npc duplicate name", "npc duplicate name")
            .mapIndexed { id, name -> NpcDefinition(id).also { it.name = name } }

        @ObjectDefinitions
        val objs = listOf("obj zero", "obj one", "obj two", "obj duplicate name", "obj duplicate name")
            .mapIndexed { id, name -> ObjectDefinition(id).also { it.name = name } }
    }
}