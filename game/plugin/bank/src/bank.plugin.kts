import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.bank.BankUtils
import org.apollo.net.message.Message

val BANK_BOOTH_ID = 2213

/**
 * Hook into the [ObjectActionMessage] and listen for when a bank booth's second action ("Open Bank") is selected.
 */
on { ObjectActionMessage::class }
        .where { option == 2 && id == BANK_BOOTH_ID }
        .then { BankAction.start(this, it, position) }

/**
 * Hook into the [NpcActionMessage] and listen for when a banker's second action ("Open Bank") is selected.
 */
on { NpcActionMessage::class }
        .where { option == 2 }
        .then {
            val npc = it.world.npcRepository[index]

            if (npc.id in BANKER_NPCS) {
                BankAction.start(this, it, npc.position)
            }
        }

/**
 * The ids of all banker [Npcs][Npc].
 */
val BANKER_NPCS = setOf(166, 494, 495, 496, 497, 498, 499, 1036, 1360, 1702, 2163, 2164, 2354, 2355, 2568, 2569, 2570)

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
        fun start(message: Message, player: Player, position: Position) {
            player.startAction(BankAction(player, position))
            message.terminate()
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
