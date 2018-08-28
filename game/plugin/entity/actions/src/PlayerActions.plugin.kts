package org.apollo.game.plugin.entity.actions

import org.apollo.game.message.impl.PlayerActionMessage
import org.apollo.game.model.event.impl.LoginEvent

on { PlayerActionMessage::class }
    .then {
        val action = it.actionAt(option)
        if (action != null) {
            it.world.submit(PlayerActionEvent(it, it.world.playerRepository[index], action))
        }

        terminate()
    }

on_player_event { LoginEvent::class }
    .then {
        it.enableAction(PlayerActionType.FOLLOW)
        it.enableAction(PlayerActionType.TRADE)
    }