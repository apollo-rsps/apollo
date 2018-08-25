package org.apollo.game.plugin.entity.spawn

import org.apollo.game.model.Animation
import org.apollo.game.model.Direction
import org.apollo.game.model.Graphic
import org.apollo.game.model.Position

fun spawnNpc(name: String, x: Int, y: Int, z: Int = 0, id: Int? = null, facing: Direction = Direction.NORTH) {
    Spawns.list += Spawn(id, name, Position(x, y, z), facing)
}

fun spawnNpc(name: String, position: Position, id: Int? = null, facing: Direction = Direction.NORTH) {
    Spawns.list += Spawn(id, name, position, facing)
}

internal data class Spawn(
    val id: Int?,
    val name: String,
    val position: Position,
    val facing: Direction,
    val spawnAnimation: Animation? = null,
    val spawnGraphic: Graphic? = null
)

internal object Spawns {
    val list = mutableListOf<Spawn>()
}
