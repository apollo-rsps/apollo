package org.apollo.game.plugin.api

import java.lang.IllegalArgumentException
import org.apollo.cache.def.ItemDefinition
import org.apollo.cache.def.NpcDefinition
import org.apollo.cache.def.ObjectDefinition
import org.intellij.lang.annotations.Language
import java.util.regex.Pattern

/**
 * Provides plugins with access to item, npc, and object definitions
 */
object Definitions {

    /**
     * Returns the [ItemDefinition] with the specified [id]. Callers of this function must perform bounds checking on
     * the [id] prior to invoking this method (i.e. verify that `id >= 0 && id < ItemDefinition.count()`).
     *
     * @throws IndexOutOfBoundsException If the id is out of bounds.
     */
    fun item(id: Int): ItemDefinition {
        return ItemDefinition.lookup(id)
    }

    /**
     * Returns the [ItemDefinition] with the specified name, performing case-insensitive matching. If multiple items
     * share the same name, the item with the lowest id is returned.
     *
     * The name may be suffixed with an explicit item id (as a way to disambiguate in the above case), by ending the
     * name with `_id`, e.g. `monks_robe_42`. If an explicit id is attached, it must be bounds checked (in the same
     * manner as [item(id: Int)][item]).
     */
    fun item(name: String): ItemDefinition? {
        return findEntity(ItemDefinition::getDefinitions, ItemDefinition::getName, name)
    }

    /**
     * Returns the [ObjectDefinition] with the specified [id]. Callers of this function must perform bounds checking on
     * the [id] prior to invoking this method (i.e. verify that `id >= 0 && id < ObjectDefinition.count()`).
     *
     * @throws IndexOutOfBoundsException If the id is out of bounds.
     */
    fun obj(id: Int): ObjectDefinition {
        return ObjectDefinition.lookup(id)
    }

    /**
     * Returns the [ObjectDefinition] with the specified name, performing case-insensitive matching. If multiple objects
     * share the same name, the object with the lowest id is returned.
     *
     * The name may be suffixed with an explicit object id (as a way to disambiguate in the above case), by ending the
     * name with `_id`, e.g. `man_2`. If an explicit id is attached, it must be bounds checked (in the same
     * manner as [object(id: Int)][object]).
     */
    fun obj(name: String): ObjectDefinition? {
        return findEntity(ObjectDefinition::getDefinitions, ObjectDefinition::getName, name)
    }

    /**
     * Returns the [NpcDefinition] with the specified [id]. Callers of this function must perform bounds checking on
     * the [id] prior to invoking this method (i.e. verify that `id >= 0 && id < NpcDefinition.count()`).
     *
     * @throws IndexOutOfBoundsException If the id is out of bounds.
     */
    fun npc(id: Int): NpcDefinition {
        return NpcDefinition.lookup(id)
    }

    /**
     * Returns the [NpcDefinition] with the specified name, performing case-insensitive matching. If multiple npcs
     * share the same name, the npc with the lowest id is returned.
     *
     * The name may be suffixed with an explicit npc id (as a way to disambiguate in the above case), by ending the
     * name with `_id`, e.g. `man_2`. If an explicit id is attached, it must be bounds checked (in the same
     * manner as [npc(id: Int)][npc]).
     */
    fun npc(name: String): NpcDefinition? {
        return findEntity(NpcDefinition::getDefinitions, NpcDefinition::getName, name)
    }

    fun npcs(@Language("RegExp") pattern: String): Sequence<NpcDefinition> {
        return findEntities(NpcDefinition::getDefinitions, NpcDefinition::getName, pattern)
    }

    /**
     * The [Regex] used to match 'names' that have an id attached to the end.
     */
    private val ID_REGEX = Regex(".+_[0-9]+$")

    private inline fun <T : Any> findEntity(
        definitionsProvider: () -> Array<T>,
        crossinline nameSupplier: T.() -> String,
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

        return findEntities(definitionsProvider, nameSupplier, name).firstOrNull()
    }

    private inline fun <T: Any> findEntities(
        definitionsProvider: () -> Array<T>,
        crossinline nameSupplier: T.() -> String,
        regexp: String
    ) : Sequence<T> {
        val definitions = definitionsProvider().asSequence()
        val pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)

        return definitions.filter {
            val name = it.nameSupplier()
            val matcher = pattern.matcher(name)

            matcher.matches()
        }
    }
}