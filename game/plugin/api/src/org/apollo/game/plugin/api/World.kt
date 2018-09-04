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

/**
 * Finds all of the [Entities][Entity] with the specified [EntityTypes][EntityType] at the specified [position], that
 * match the provided [predicate].
 *
 * ```
 * const val GOLD_COINS = 995
 * ...
 *
 * val allCoins: Sequence<GroundItem> = region.find(position, EntityType.GROUND_ITEM) { item -> item.id == GOLD_COINS }
 * ```
 */
fun <T : Entity> Region.find(position: Position, vararg types: EntityType, predicate: (T) -> Boolean): Sequence<T> {
    return getEntities<T>(position, *types).asSequence().filter(predicate)
}

/**
 * Finds the first [GameObject]s with the specified [id] at the specified [position].
 *
 * Note that the iteration order of entities in a [Region] is not defined - this function should not be used if there
 * may be more than [GameObject] with the specified [id] (see [Region.findObjects]).
 */
fun Region.findObject(position: Position, id: Int): GameObject? {
    return find<GameObject>(position, DYNAMIC_OBJECT, STATIC_OBJECT) { it.id == id }
        .firstOrNull()
}

/**
 * Finds **all** [GameObject]s with the specified [id] at the specified [position].
 */
fun Region.findObjects(position: Position, id: Int): Sequence<GameObject> {
    return find(position, DYNAMIC_OBJECT, STATIC_OBJECT) { it.id == id }
}

/**
 * Finds the first [GameObject]s with the specified [id] at the specified [position].
 *
 * Note that the iteration order of entities in a [Region] is not defined - this function should not be used if there
 * may be more than [GameObject] with the specified [id] (see [World.findObjects]).
 */
fun World.findObject(position: Position, id: Int): GameObject? {
    return regionRepository.fromPosition(position).findObject(position, id)
}

/**
 * Finds **all** [GameObject]s with the specified [id] at the specified [position].
 */
fun World.findObjects(position: Position, id: Int): Sequence<GameObject> {
    return regionRepository.fromPosition(position).findObjects(position, id)
}

/**
 * Removes the specified [GameObject] from the world, replacing it with [replacement] object for [delay] **pulses**.
 */
fun World.replaceObject(obj: GameObject, replacement: Int, delay: Int) {
    val replacementObj = DynamicGameObject.createPublic(this, replacement, obj.position, obj.type, obj.orientation)

    schedule(ExpireObjectTask(this, obj, replacementObj, delay))
}

/**
 * A [ScheduledTask] that temporarily replaces the [existing] [GameObject] with the [replacement] [GameObject] for the
 * specified [duration].
 *
 * @param existing The [GameObject] that already exists and should be replaced.
 * @param replacement The [GameObject] to replace the [existing] object with.
 * @param duration The time, in **pulses**, for the [replacement] object to exist in the game world.
 */
private class ExpireObjectTask(
    private val world: World,
    private val existing: GameObject,
    private val replacement: GameObject,
    private val duration: Int
) : ScheduledTask(0, true) {

    private var respawning: Boolean = false

    override fun execute() {
        val region = world.regionRepository.fromPosition(existing.position)

        if (!respawning) {
            world.spawn(replacement)
            respawning = true
            setDelay(duration)
        } else {
            region.removeEntity(replacement)
            world.spawn(existing)
            stop()
        }
    }
}