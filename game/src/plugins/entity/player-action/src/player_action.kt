import org.apollo.game.message.impl.SetPlayerActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.model.event.PlayerEvent
import java.util.*

enum class PlayerActionType(val displayName: String, val slot: Int, val primary: Boolean = true) {
    ATTACK("Attack", 2),
    CHALLENGE("Challenge", 2),
    FOLLOW("Follow", 4),
    TRADE("Trade with", 5)
}

class PlayerActionEvent(player: Player, val target: Player, val action: PlayerActionType) : PlayerEvent(player)

private val playerActionsMap = mutableMapOf<Player, EnumSet<PlayerActionType>>()
private val Player.actions: EnumSet<PlayerActionType>
    get() = playerActionsMap.computeIfAbsent(this, { EnumSet.noneOf(PlayerActionType::class.java) })

fun Player.enableAction(action: PlayerActionType) {
    send(SetPlayerActionMessage(action.displayName, action.slot, action.primary))
    actions.add(action)
}

fun Player.disableAction(action: PlayerActionType) {
    send(SetPlayerActionMessage("null", action.slot, action.primary))
    actions.remove(action)
}

fun Player.actionEnabled(action: PlayerActionType): Boolean {
    return actions.contains(action)
}

fun Player.actionAt(slot: Int): PlayerActionType? {
    return actions.find { it.slot == slot }
}