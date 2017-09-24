package org.apollo.game.plugin.util.lookup

import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition

fun lookup_object(name: String): ObjectDefinition? {
    return find_entity(ObjectDefinition::getDefinitions, ObjectDefinition::getName, name)
}

fun lookup_npc(name: String): NpcDefinition? {
    return find_entity(NpcDefinition::getDefinitions, NpcDefinition::getName, name)
}

fun lookup_item(name: String): ItemDefinition? {
    return find_entity(ItemDefinition::getDefinitions, ItemDefinition::getName, name)
}

/**
 * The [Regex] used to match 'names' that have an id attached to the end.
 */
private val ID_REGEX = Regex(".+_[0-9]+$")

private fun <T : Any> find_entity(definitionsProvider: () -> Array<T>,
                                  nameSupplier: T.() -> String,
                                  name: String): T? {
    val definitions = definitionsProvider.invoke()

    if (ID_REGEX matches name) {
        val id = name.substring(name.lastIndexOf('_') + 1, name.length).toInt()
        return definitions.getOrNull(id)
    }

    val normalizedName = name.replace('_', ' ')

    return definitions.firstOrNull { it.nameSupplier().equals(normalizedName, true) }
}