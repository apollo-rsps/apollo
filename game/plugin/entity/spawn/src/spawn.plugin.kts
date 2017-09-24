import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.entity.Npc
import org.apollo.game.plugin.entity.spawn.Spawns
import org.apollo.game.plugin.util.lookup.lookup_npc

start { world ->
    Spawns.list.forEach {
        val definition = it.id?.let { NpcDefinition.lookup(it) } ?: lookup_npc(it.name) ?:
            throw IllegalArgumentException("Invalid NPC name or ID ${it.name}, ${it.id}")

        val npc = Npc(world, definition.id, it.position)
        npc.turnTo(it.position.step(1, it.facing))

        it.spawnAnimation?.let(npc::playAnimation)
        it.spawnGraphic?.let(npc::playGraphic)

        world.register(npc)
    }
}
