package org.apollo.plugin.entity.pathing

import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.path.SimplePathfindingAlgorithm

/**
 * Adds a path from this [Mob] to the [target] [Entity], with the final position determined by the [facing] [Direction].
 */
fun Mob.walkTo(target: Entity, facing: Direction? = null) {
    val (width, length) = bounds(this)
    val (targetWidth, targetLength) = bounds(target)

    val direction = facing ?: Direction.between(position, target.position)
    val dx = direction.deltaX()
    val dy = direction.deltaY()

    val targetX = if (dx <= 0) target.position.x else target.position.x + targetWidth - 1
    val targetY = if (dy <= 0) target.position.y else target.position.y + targetLength - 1
    val offsetX = if (dx < 0) -width else if (dx > 0) 1 else 0
    val offsetY = if (dy < 0) -length else if (dy > 0) 1 else 0

    walkTo(Position(targetX + offsetX, targetY + offsetY, position.height))
}

/**
 * Adds a path for this [Mob] to the target [Mob]s last position.
 */
fun Mob.walkBehind(target: Mob) {
    walkTo(target, target.lastDirection.opposite())
}

/**
 * Adds a path from this [Mob] to the [target] [Position], ending the path as soon as [positionPredicate] returns
 * `false` (if provided).
 */
fun Mob.walkTo(target: Position, positionPredicate: ((Position) -> Boolean)? = null) {
    if (position == target) {
        return
    }

    val pathfinder = SimplePathfindingAlgorithm(world.collisionManager)
    val path = pathfinder.find(position, target)

    if (positionPredicate == null) {
        path.forEach(walkingQueue::addStep)
    } else {
        for (step in path) {
            if (!positionPredicate(step)) {
                return
            }

            walkingQueue.addStep(step)
        }
    }
}

/**
 * Returns the bounding size of the specified [Entity], in [x-size, y-size] format.
 */
private fun bounds(target: Entity): Pair<Int, Int> = Pair(target.width, target.length)