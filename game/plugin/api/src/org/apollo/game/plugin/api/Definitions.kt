package org.apollo.game.plugin.api

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import java.lang.IllegalArgumentException

object Definitions {
    fun item(id: Int): ItemDefinition? {
        return ItemDefinition.lookup(id)
    }

    fun item(name: String): ItemDefinition? {
        return findEntity(ItemDefinition::getDefinitions, ItemDefinition::getName, name)
    }

    fun obj(id: Int): ObjectDefinition? {
        return ObjectDefinition.lookup(id)
    }

    fun obj(name: String): ObjectDefinition? {
        return findEntity(ObjectDefinition::getDefinitions, ObjectDefinition::getName, name)
    }

    fun npc(id: Int): NpcDefinition? {
        return NpcDefinition.lookup(id)
    }

    fun npc(name: String): NpcDefinition? {
        return findEntity(NpcDefinition::getDefinitions, NpcDefinition::getName, name)
    }

    /**
     * The [Regex] used to match 'names' that have an id attached to the end.
     */
    private val ID_REGEX = Regex(".+_[0-9]+$")

    private fun <T : Any> findEntity(
        definitionsProvider: () -> Array<T>,
        nameSupplier: T.() -> String,
        name: String
    ): T? {
        val definitions = definitionsProvider()

        if (ID_REGEX matches name) {
            val id = name.substring(name.lastIndexOf('_') + 1, name.length).toIntOrNull()

            if (id == null || id >= definitions.size) {
                throw IllegalArgumentException("Error while searching for definition: invalid id suffix in $name.")
            }

            return definitions[id]
        }

        val normalizedName = name.replace('_', ' ')
        return definitions.firstOrNull { it.nameSupplier().equals(normalizedName, ignoreCase = true) }
    }

}