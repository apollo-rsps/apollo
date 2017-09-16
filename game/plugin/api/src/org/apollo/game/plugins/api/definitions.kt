package org.apollo.game.plugins.api

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition

object Definitions {
    fun item(id: Int): ItemDefinition? {
        return ItemDefinition.lookup(id)
    }

    fun obj(id: Int): ObjectDefinition? {
        return ObjectDefinition.lookup(id)
    }

    fun npc(id: Int): NpcDefinition? {
        return NpcDefinition.lookup(id)
    }
}