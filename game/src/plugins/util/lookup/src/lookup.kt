import org.apollo.cache.def.*

fun lookup_object(name: String): ObjectDefinition? {
    val definitions = ObjectDefinition.getDefinitions()
    return definitions.filter { name.equals(it.name, true) }.firstOrNull()
}

fun lookup_npc(name: String): NpcDefinition? {
    val definitions = NpcDefinition.getDefinitions()
    return definitions.filter { name.equals(it.name, true) }.firstOrNull()
}

fun lookup_item(name: String): ItemDefinition? {
    val definitions = ItemDefinition.getDefinitions()
    return definitions.filter { name.equals(it.name, true) }.firstOrNull()
}

