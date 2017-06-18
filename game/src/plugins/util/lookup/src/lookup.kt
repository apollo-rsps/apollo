import org.apollo.cache.def.*

fun lookup_object(name: String): ObjectDefinition? {
    return find_entity(ObjectDefinition::getDefinitions, ObjectDefinition::getName, name)
}

fun lookup_npc(name: String): NpcDefinition? {
    return find_entity(NpcDefinition::getDefinitions, NpcDefinition::getName, name)
}

fun lookup_item(name: String): ItemDefinition? {
    return find_entity(ItemDefinition::getDefinitions, ItemDefinition::getName, name)
}

private fun <T : Any> find_entity(definitionsProvider: () -> Array<T>,
                                  nameSupplier: T.() -> String,
                                  name: String): T? {

    val normalizedName = name.replace('_', ' ')
    val definitions = definitionsProvider.invoke();
    val matcher: (T) -> Boolean = { it.nameSupplier().equals(normalizedName, true) }

    return definitions.filter(matcher).firstOrNull()
}