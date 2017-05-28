import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.entity.Npc

start { world ->
    Spawns.list.forEach {
        val definition = if (it.id != null) NpcDefinition.lookup(it.id) else lookup_npc(it.name)
        if (definition == null) {
            throw IllegalArgumentException("Invalid NPC name or ID ${it.name}, ${it.id}")
        }

        val npc = Npc(world, definition.id, it.position)
        npc.turnTo(it.position.step(1, it.facing))

        if (it.spawnAnimation != null) {
            npc.playAnimation(it.spawnAnimation)
        }

        if (it.spawnGraphic != null) {
            npc.playGraphic(it.spawnGraphic)
        }

        world.register(npc)
    }
}
