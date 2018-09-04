import java.util.*
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player

class ExpiredProspectingAction(
    mob: Player,
    position: Position
) : DistancedAction<Player>(DELAY, true, mob, position, ORE_SIZE) {

    companion object {
        private const val DELAY = 0
        private const val ORE_SIZE = 1

        /**
         * Starts a [ExpiredProspectingAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player) {
            val action = ExpiredProspectingAction(player, message.position)
            player.startAction(action)

            message.terminate()
        }
    }

    override fun executeAction() {
        mob.sendMessage("There is currently no ore available in this rock.")
        stop()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpiredProspectingAction
        return mob == other.mob && position == other.position
    }

    override fun hashCode(): Int = Objects.hash(mob, position)
}