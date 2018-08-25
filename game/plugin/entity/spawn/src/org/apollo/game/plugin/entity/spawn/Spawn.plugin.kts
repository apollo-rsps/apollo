package org.apollo.game.plugin.entity.spawn

import org.apollo.game.model.entity.Npc
import org.apollo.game.plugin.api.Definitions

start { world ->
    for ((id, name, position, facing, animation, graphic) in Spawns.list) {
        val definition = requireNotNull(id?.let(Definitions::npc) ?: Definitions.npc(name)) {
            "Could not find an Npc named $name to spawn."
        }

        val npc = Npc(world, definition.id, position).apply {
            turnTo(position.step(1, facing))
            animation?.let(::playAnimation)
            graphic?.let(::playGraphic)
        }

        world.register(npc)
    }
}
