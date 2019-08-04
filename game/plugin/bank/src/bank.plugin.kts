import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.bank.BankUtils
import org.apollo.game.plugin.api.Definitions.npcs
import org.apollo.game.plugin.kotlin.message.action.npc.NpcAction
import org.apollo.game.plugin.kotlin.message.action.obj.InteractiveObject
import org.apollo.game.plugin.kotlin.message.action.obj.ObjectAction
import org.apollo.net.message.Message

enum class QuickBankObject(override val id: Int) : InteractiveObject {
    BankBooth(2213)
}

on(ObjectAction, "Open Bank", objects = QuickBankObject.values()) {
    BankAction.start(player, target.position)
}

val bankerNpcs = npcs("(Gnome )?Banker")

/**
 * Hook into the [NpcActionMessage] and listen for when a banker's second action ("Open Bank") is selected.
 */
on(NpcAction, "Open Bank", bankerNpcs) {
    BankAction.start(player, target.position)
}

/**
 * A [DistancedAction] that opens a [Player]'s bank when they get close enough to a booth or banker.
 *
 * @property position The [Position] of the booth/[Npc].
 */
class BankAction(player: Player, position: Position) : DistancedAction<Player>(0, true, player, position, DISTANCE) {

    companion object {

        /**
         * The distance threshold that must be reached before the bank interface is opened.
         */
        const val DISTANCE = 1

        /**
         * Starts a [BankAction] for the specified [Player], terminating the [Message] that triggered.
         */
        fun start(player: Player, position: Position) {
            player.startAction(BankAction(player, position))
        }
    }

    override fun executeAction() {
        mob.turnTo(position)
        BankUtils.openBank(mob)
        stop()
    }

    override fun equals(other: Any?): Boolean {
        return other is BankAction && position == other.position
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }
}
