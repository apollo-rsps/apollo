package org.apollo.game.plugin.entity.spawn

import org.apollo.game.model.Animation
import org.apollo.game.model.Direction
import org.apollo.game.model.Graphic
import org.apollo.game.model.Position

data class Spawn(val id: Int?, val name: String, val position: Position, val facing: Direction,
                 val spawnAnimation: Animation? = null, val spawnGraphic: Graphic? = null)

object Spawns {
    val list = mutableListOf<Spawn>()
}

fun npc_spawn(name: String, x: Int, y: Int, z: Int = 0, id: Int? = null, facing: Direction = Direction.NORTH) {
    Spawns.list.add(Spawn(id, name, Position(x, y, z), facing))
}
