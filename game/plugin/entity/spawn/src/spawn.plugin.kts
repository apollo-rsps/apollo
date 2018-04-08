
import org.apollo.game.model.entity.Npc
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.entity.spawn.Spawns

start { world ->
    Spawns.list.forEach { spawn ->
        val definition = spawn.id?.let(Definitions::npc) ?: Definitions.npc(spawn.name)
        ?: throw IllegalArgumentException("Invalid NPC name or ID ${spawn.name}, ${spawn.id}")

        val npc = Npc(world, definition.id, spawn.position)
        npc.turnTo(spawn.position.step(1, spawn.facing))

        spawn.spawnAnimation?.let(npc::playAnimation)
        spawn.spawnGraphic?.let(npc::playGraphic)

        world.register(npc)
    }
}
