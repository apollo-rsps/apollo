package org.apollo.game.plugin.entity.actions

import java.util.*
import org.apollo.game.message.impl.SetPlayerActionMessage
import org.apollo.game.model.entity.Player

fun Player.enableAction(action: PlayerActionType) {
    send(SetPlayerActionMessage(action.displayName, action.slot, action.primary))
    actions += action
}

fun Player.disableAction(action: PlayerActionType) {
    send(SetPlayerActionMessage("null", action.slot, action.primary))
    actions -= action
}

fun Player.actionEnabled(action: PlayerActionType): Boolean {
    return action in actions
}

fun Player.actionAt(slot: Int): PlayerActionType? {
    return actions.find { it.slot == slot }
}

private val playerActionsMap = mutableMapOf<Player, EnumSet<PlayerActionType>>()

private val Player.actions: EnumSet<PlayerActionType>
    get() = playerActionsMap.computeIfAbsent(this) { EnumSet.noneOf(PlayerActionType::class.java) }
