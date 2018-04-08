package org.apollo.game.plugin.testing.junit.api.interactions

import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.obj.StaticGameObject

/**
 * Spawn a new static game object into the world with the given id and position.
 */
fun World.spawnObject(id: Int, position: Position): GameObject {
    val obj = StaticGameObject(this, id, position, 0, 0)

    spawn(obj)

    return obj
}
