package org.apollo.plugin.entity.following

import org.apollo.game.action.Action
import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.net.message.Message
import org.apollo.plugin.entity.pathing.walkBehind
import org.apollo.plugin.entity.pathing.walkTo

class FollowAction(player: Player, private val target: Player) : Action<Player>(0, true, player) {
    var lastPosition: Position? = null

    companion object {
        fun start(player: Player, target: Player, message: Message? = null) {
            player.startAction(FollowAction(player, target))
            message?.terminate()
        }
    }

    override fun execute() {
        if (!target.isActive) {
            stop()
            return
        }

        mob.interactingMob = target

        if (target.position == lastPosition) {
            return
        }

        val distance = mob.position.getDistance(target.position)
        if (distance >= 15) {
            stop()
            return
        }

        if (mob.position == target.position) {
            val directions = Direction.NESW
            val directionOffset = (Math.random() * directions.size).toInt()

            mob.walkTo(target.position.step(1, directions[directionOffset]))
        } else {
            mob.walkBehind(target)
            lastPosition = target.position
        }
    }
}