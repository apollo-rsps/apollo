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
import java.util.Optional
import java.util.stream.Stream

fun <T : Entity> Region.find(position: Position, predicate: (T) -> Boolean, vararg types: EntityType): Stream<T> {
    val result = getEntities<T>(position, *types)
    return result.stream().filter(predicate::invoke)
}

fun Region.findObjects(position: Position, id: Int): Stream<GameObject> {
    return find(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT)
}

fun Region.findObject(position: Position, id: Int): Optional<GameObject> {
    return find<GameObject>(position, { it.id == id }, DYNAMIC_OBJECT, STATIC_OBJECT)
        .findFirst()
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

fun World.expireObject(obj: GameObject, replacement: Int, respawnDelay: Int) {
    val replacementObj = DynamicGameObject.createPublic(this, replacement, obj.position, obj.type, obj.orientation)

    schedule(ExpireObjectTask(this, obj, replacementObj, respawnDelay))
}
