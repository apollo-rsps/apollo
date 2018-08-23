package org.apollo.game.plugin.api

import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.area.Region
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.EntityType.DYNAMIC_OBJECT
import org.apollo.game.model.entity.EntityType.STATIC_OBJECT
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.scheduling.ScheduledTask

fun <T : Entity> Region.find(position: Position, predicate: (T) -> Boolean, vararg types: EntityType): Sequence<T> {
    return getEntities<T>(position, *types).asSequence().filter(predicate)
}

fun Region.findObjects(position: Position, id: Int): Sequence<GameObject> {
    return find(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT)
}

fun Region.findObject(position: Position, id: Int): GameObject? {
    return find<GameObject>(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT).firstOrNull()
}

fun World.findObject(position: Position, objectId: Int): GameObject? {
    return regionRepository.fromPosition(position).findObject(position, objectId)
}

fun World.findObjects(position: Position, id: Int): Sequence<GameObject> {
    return regionRepository.fromPosition(position).findObjects(position, id)
}

fun World.expireObject(obj: GameObject, replacement: Int, respawnDelay: Int) {
    val replacementObj = DynamicGameObject.createPublic(this, replacement, obj.position, obj.type, obj.orientation)

    schedule(ExpireObjectTask(this, obj, replacementObj, respawnDelay))
}


class ExpireObjectTask(
    private val world: World,
    private val existing: GameObject,
    private val replacement: GameObject,
    private val respawnDelay: Int
) : ScheduledTask(0, true) {

    private var respawning: Boolean = false

    override fun execute() {
        val region = world.regionRepository.fromPosition(existing.position)

        if (!respawning) {
            world.spawn(replacement)
            respawning = true
            setDelay(respawnDelay)
        } else {
            region.removeEntity(replacement)
            world.spawn(existing)
            stop()
        }
    }
}