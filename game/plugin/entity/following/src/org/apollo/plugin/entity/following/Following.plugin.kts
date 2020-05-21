package org.apollo.plugin.entity.following

import org.apollo.game.plugin.entity.actions.PlayerActionEvent
import org.apollo.game.plugin.entity.actions.PlayerActionType

on_player_event { PlayerActionEvent::class }
    .where { action == PlayerActionType.FOLLOW }
    .then { player ->
        FollowAction.start(player, target)
        terminate()
    }