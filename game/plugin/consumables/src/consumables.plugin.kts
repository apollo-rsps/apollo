import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncAction
import org.apollo.game.message.impl.ItemOptionMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.entity.Player
import org.apollo.net.message.Message
import org.apollo.plugin.consumables.*

on { ItemOptionMessage::class }
        .where { option == 1 && isConsumable(id) }
        .then {
            ConsumeAction.start(this, it, lookupConsumable(id), slot)
        }

class ConsumeAction(val consumable: Consumable, player: Player, val slot: Int) :
        AsyncAction<Player>(CONSUME_STARTUP_DELAY, true, player) {

    companion object {
        const val CONSUME_ANIMATION_ID = 829
        const val CONSUME_STARTUP_DELAY = 2

        /**
         * Starts a [ConsumeAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: Message, player: Player, consumable: Consumable, slot: Int) {
            player.startAction(ConsumeAction(consumable, player, slot))
            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        consumable.consume(mob, slot)
        mob.playAnimation(Animation(CONSUME_ANIMATION_ID))
        wait(consumable.delay)
        stop()
    }
}
