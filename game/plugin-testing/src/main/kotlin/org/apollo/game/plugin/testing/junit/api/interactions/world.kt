package org.apollo.game.plugin.testing.junit.api.interactions

import org.apollo.cache.def.NpcDefinition
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
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

/**
 * Spawns a new NPC with the minimum set of dependencies required to function correctly in the world.
 */
fun World.spawnNpc(id: Int, position: Position): Npc {
    val definition = NpcDefinition(id)
    val npc = Npc(this, position, definition, arrayOfNulls(4))
    val region = regionRepository.fromPosition(position)
    val npcs = npcRepository

    npcs.add(npc)
    region.addEntity(npc)

    return npc
}