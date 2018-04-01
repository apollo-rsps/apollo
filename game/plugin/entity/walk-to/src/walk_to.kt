package org.apollo.plugin.entity.walkto

import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.model.entity.path.SimplePathfindingAlgorithm

private fun bounds(target: Entity): Pair<Int, Int> = when (target) {
    is GameObject -> {
        val orientation = Direction.WNES[target.orientation]
        val rotated = (orientation == Direction.WEST || orientation == Direction.EAST)

        val width = if (rotated) target.definition.length else target.definition.width
        val height = if (rotated) target.definition.width else target.definition.length

        Pair(width, height)
    }
    is Npc -> Pair(target.definition.size, target.definition.size)
    is Player -> Pair(1, 1)
    else -> error("Invalid entity type")
}

fun Mob.walkTo(target: Entity, positioningDirection: Direction? = null) {
    val (sourceWidth, sourceHeight) = bounds(target)
    val (targetWidth, targetHeight) = bounds(target)

    val direction = positioningDirection ?: Direction.between(position, target.position)
    val dx = direction.deltaX()
    val dy = direction.deltaY()

    val targetX = if (dx <= 0) target.position.x else target.position.x + targetWidth - 1
    val targetY = if (dy <= 0) target.position.y else target.position.y + targetHeight - 1
    val offsetX = if (dx < 0) -sourceWidth else if (dx > 0) 1 else 0
    val offsetY = if (dy < 0) -sourceHeight else if (dy > 0) 1 else 0

    walkTo(Position(targetX + offsetX, targetY + offsetY, position.height))
}

fun Mob.walkBehind(target: Mob) {
    walkTo(target, target.lastDirection.opposite())
}

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
            if (!positionPredicate.invoke(step)) {
                return
            }

            walkingQueue.addStep(step)
        }
    }
}